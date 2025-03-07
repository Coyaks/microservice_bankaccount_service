package com.skoy.bankaccount_service.service;

import com.skoy.bankaccount_service.dto.*;
import com.skoy.bankaccount_service.enums.AccountTypeEnum;
import com.skoy.bankaccount_service.enums.CustomerTypeEnum;
import com.skoy.bankaccount_service.enums.TransactionTypeEnum;
import com.skoy.bankaccount_service.mapper.BankAccountMapper;
import com.skoy.bankaccount_service.model.BankAccount;
import com.skoy.bankaccount_service.model.BankAccountResponse;
import com.skoy.bankaccount_service.repository.IBankAccountRepository;
import com.skoy.bankaccount_service.utils.ApiResponse;
import com.skoy.bankaccount_service.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BankAccountService implements IBankAccountService {

    private final IBankAccountRepository repository;
    private final WebClient.Builder webClientBuilder;
    private static final Logger log = LoggerFactory.getLogger(BankAccountService.class);

    @Value("${services.customer}")
    private String customerServiceUrl;

    @Value("${services.credit}")
    private String creditServiceUrl;



    @Override
    public Mono<BankAccountDTO> create(BankAccountDTO bankAccountDTO) {

        if (bankAccountDTO.getAvailableBalance().compareTo(Constants.MIN_OPENING_BALANCE) < 0) {
            return Mono.error(new RuntimeException("El monto de apertura no puede ser menor al monto mínimo de apertura"));
        }

        return getCustomer(bankAccountDTO.getCustomerId())
                .flatMap(customer -> {
                    if (customer == null) return Mono.error(new RuntimeException("Cliente no encontrado"));

                    return hasOverdueDebt(bankAccountDTO.getCustomerId())
                            .flatMap(hasDebt -> {
                                if (hasDebt) {
                                    return Mono.error(new RuntimeException("Un cliente no podrá adquirir un producto si posee alguna deuda vencida en algún producto de crédito"));
                                }

                                // Verificar tipo de cliente y tipo de cuenta
                                if (customer.getCustomerType().equals(CustomerTypeEnum.EMPRESARIAL) &&
                                        (bankAccountDTO.getAccountType().equals(AccountTypeEnum.AHORRO)
                                                || bankAccountDTO.getAccountType().equals(AccountTypeEnum.PLAZO_FIJO))) {
                                    return Mono.error(new RuntimeException("Un cliente empresarial no puede tener una cuenta de ahorro o de plazo fijo"));
                                }

                                if (customer.getCustomerType() == CustomerTypeEnum.PERSONAL_VIP) {
                                    // Verificar si el cliente tiene tarjetas de crédito
                                    return hasCreditCard(bankAccountDTO.getCustomerId())
                                            .flatMap(hasCreditCard -> {
                                                if (!hasCreditCard) {
                                                    return Mono.error(new RuntimeException("El cliente debe tener una tarjeta de crédito con el banco para solicitar este producto"));
                                                }
                                                // Check accounts customer
                                                return checkCustomerAccountsAndSave(customer, bankAccountDTO);
                                            });
                                } else if (customer.getCustomerType() == CustomerTypeEnum.EMPRESARIAL_PYME) {
                                    // Verificar si el cliente tiene tarjetas de crédito
                                    return hasCreditCard(bankAccountDTO.getCustomerId())
                                            .flatMap(hasCreditCard -> {
                                                if (!hasCreditCard) {
                                                    return Mono.error(new RuntimeException("El cliente debe tener una tarjeta de crédito con el banco para solicitar este producto"));
                                                }
                                                // Check accounts customer
                                                return checkCustomerAccountsAndSave(customer, bankAccountDTO);
                                            });
                                } else {
                                    if (customer.getCustomerType() == CustomerTypeEnum.EMPRESARIAL &&
                                            bankAccountDTO.getAccountType() == AccountTypeEnum.CORRIENTE &&
                                            bankAccountDTO.getMaintenanceCommission().compareTo(BigDecimal.ZERO) <= 0) {
                                        return Mono.error(new RuntimeException("Un cliente empresarial no puede tener una cuenta corriente con comisión de mantenimiento menor o igual a cero"));
                                    }
                                    // Check accounts customer
                                    return checkCustomerAccountsAndSave(customer, bankAccountDTO);
                                }
                            });
                })
                .doOnError(error -> log.error("Error al validar cliente: {}", error.getMessage()));
    }


    private Mono<CustomerDTO> getCustomer(String customerId) {
        String customerUri = customerServiceUrl + "/customers/" + customerId;
        return webClientBuilder.build()
                .get()
                .uri(customerUri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<CustomerDTO>>() {
                })
                .map(ApiResponse::getData);
    }

    private Mono<Boolean> hasCreditCard(String customerId) {
        String url = creditServiceUrl + "/credits/customer/" + customerId;
        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {
                })
                .map(creditCards -> !creditCards.isEmpty());
    }

    private Mono<Boolean> hasOverdueDebt(String customerId) {
        String url = creditServiceUrl + "/credits/debt/" + customerId;
        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<Object>>() {
                })
                .map(ApiResponse::isOk);
    }


    private Mono<BankAccountDTO> checkCustomerAccountsAndSave(CustomerDTO customer, BankAccountDTO bankAccountDTO) {
        return repository.findAllByCustomerId(bankAccountDTO.getCustomerId())
                .collectList()
                .flatMap(rspAccounts -> {
                    System.out.println("Existing Accounts: " + rspAccounts);
                    if (customer.getCustomerType().equals(CustomerTypeEnum.PERSONAL)) {
                        boolean hasAccountType = rspAccounts.stream().anyMatch(account -> account.getAccountType().equals(bankAccountDTO.getAccountType()));
                        if (hasAccountType) {
                            return Mono.error(new RuntimeException("El cliente como máximo puede tener una cuenta de este tipo"));
                        }
                    }
                    BankAccount bankAccount = BankAccountMapper.toEntity(bankAccountDTO);
                    if (bankAccountDTO.getAccountType() == AccountTypeEnum.AHORRO || bankAccountDTO.getAccountType() == AccountTypeEnum.PLAZO_FIJO) {
                        bankAccount.setMaintenanceCommission(BigDecimal.valueOf(0));
                    } else {
                        bankAccount.setMaintenanceCommission(Constants.MAINTENANCE_COMMISSION);
                    }
                    return repository.save(bankAccount)
                            .map(BankAccountMapper::toDto);
                });
    }

    @Override
    public Flux<BankAccountDTO> findAll() {
        return repository.findAll()
                .map(BankAccountMapper::toDto);
    }

    @Override
    public Mono<BankAccountDTO> findById(String id) {
        return repository.findById(id)
                .map(BankAccountMapper::toDto);
    }

    @Override
    public Mono<BankAccountDTO> update(String id, BankAccountDTO accountDTO) {
        return repository.findById(id)
                .flatMap(item -> {
                    item = BankAccountMapper.toEntity(accountDTO);
                    item.setUpdatedAt(LocalDateTime.now());
                    return repository.save(item);
                })
                .map(BankAccountMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        return repository.deleteById(id);
    }

    @Override
    public Flux<BankAccountDTO> findAllByCustomerId(String customerId) {
        return repository.findAllByCustomerId(customerId);

    }


//    @Override
//    public Mono<BankAccount> updateBalance(UpdateBalanceDTO updateBalanceDTO) {
//        if (updateBalanceDTO.getProductTypeId() == null || updateBalanceDTO.getProductTypeId().isEmpty()) {
//            return Mono.error(new IllegalArgumentException("El ID de la cuenta bancaria no puede estar vacío"));
//        }
//
//        return repository.findById(updateBalanceDTO.getProductTypeId())
//                .flatMap(account -> {
//                    BigDecimal newBalance;
//                    if (updateBalanceDTO.getTransactionType() == TransactionTypeEnum.DEPOSIT) {
//                        newBalance = account.getAvailableBalance().add(updateBalanceDTO.getAmount());
//                    } else if (updateBalanceDTO.getTransactionType() == TransactionTypeEnum.WITHDRAWAL) {
//                        newBalance = account.getAvailableBalance().subtract(updateBalanceDTO.getAmount());
//                    } else {
//                        return Mono.error(new IllegalArgumentException("Tipo de transacción no válido"));
//                    }
//
//                    // Incrementar el contador de transacciones
//                    account.setTransactionCount(account.getTransactionCount() + 1);
//
//                    // Verificar si se ha superado el número máximo de transacciones sin comisión
//                    if (account.getTransactionCount() > account.getMaxFreeTransactions()) {
//                        BigDecimal commission = updateBalanceDTO.getAmount().multiply(account.getTransactionCommission().divide(BigDecimal.valueOf(100)));
//                        newBalance = newBalance.subtract(commission);
//                    }
//
//                    account.setAvailableBalance(newBalance);
//                    return repository.save(account);
//                })
//                .switchIfEmpty(Mono.error(new IllegalArgumentException("Cuenta bancaria no encontrada")));
//    }


    @Override
    public Mono<BankAccountResponse> updateBalance(UpdateBalanceDTO updateBalanceDTO) {
        if (updateBalanceDTO.getProductTypeId() == null || updateBalanceDTO.getProductTypeId().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El ID de la cuenta bancaria no puede estar vacío"));
        }

        return repository.findById(updateBalanceDTO.getProductTypeId())
                .flatMap(account -> {
                    BigDecimal newBalance;
                    final BigDecimal commission;

                    if (updateBalanceDTO.getTransactionType() == TransactionTypeEnum.DEPOSIT) {
                        newBalance = account.getAvailableBalance().add(updateBalanceDTO.getAmount());
                    } else if (updateBalanceDTO.getTransactionType() == TransactionTypeEnum.WITHDRAWAL) {
                        newBalance = account.getAvailableBalance().subtract(updateBalanceDTO.getAmount());
                    } else {
                        return Mono.error(new IllegalArgumentException("Tipo de transacción no válido"));
                    }

                    // Incrementar el contador de transacciones
                    account.setTransactionCount(account.getTransactionCount() + 1);

                    // Verificar si se ha superado el número máximo de transacciones sin comisión
                    if (account.getTransactionCount() > account.getMaxFreeTransactions()) {
                        commission = updateBalanceDTO.getAmount().multiply(account.getTransactionCommission().divide(BigDecimal.valueOf(100)));
                        newBalance = newBalance.subtract(commission);
                    } else {
                        commission = BigDecimal.ZERO;
                    }

                    account.setAvailableBalance(newBalance);
                    return repository.save(account)
                            .map(savedAccount -> new BankAccountResponse(savedAccount, commission));
                })
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Cuenta bancaria no encontrada")));
    }

    @Override
    public Mono<BigDecimal> getAvailableBalanceByCustomerId(GetAvailableBalanceDTO getAvailableBalanceDTO) {
        return webClientBuilder.build()
                .get()
                .uri(customerServiceUrl + "/customers/" + getAvailableBalanceDTO.getCustomerId())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<CustomerDTO>>() {
                })
                .flatMap(response -> {
                    if (response.getData() == null) return Mono.error(new RuntimeException("Cliente no encontrado"));
                    return repository.findAllByCustomerId(getAvailableBalanceDTO.getCustomerId())
                            .filter(account -> account.getAccountType().equals(getAvailableBalanceDTO.getAccountType()))
                            .singleOrEmpty()
                            .map(BankAccountDTO::getAvailableBalance)
                            .switchIfEmpty(Mono.error(new RuntimeException("No se encontró una cuenta con el tipo especificado para el cliente")));
                });
    }

    @Override
    public Mono<ApiResponse<Object>> transfer(TransferDTO transferDTO) {
        return repository.findById(transferDTO.getSourceAccountId())
                .flatMap(sourceAccount -> {
                    if (sourceAccount.getAvailableBalance().compareTo(transferDTO.getAmount()) < 0) {
                        return Mono.error(new RuntimeException("Saldo insuficiente en la cuenta de origen"));
                    }
                    return repository.findById(transferDTO.getDestinationAccountId())
                            .flatMap(destinationAccount -> {
                                sourceAccount.setAvailableBalance(sourceAccount.getAvailableBalance().subtract(transferDTO.getAmount()));
                                destinationAccount.setAvailableBalance(destinationAccount.getAvailableBalance().add(transferDTO.getAmount()));
                                return repository.save(sourceAccount)
                                        .then(repository.save(destinationAccount))
                                        .then(Mono.just(new ApiResponse<>("Transferencia realizada con éxito", null, Constants.STATUS_OK)));
                            });
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Cuenta de origen no encontrada")))
                .onErrorResume(e -> Mono.just(new ApiResponse<>("Error en la transferencia: " + e.getMessage(), null, Constants.STATUS_E500)));
    }

}