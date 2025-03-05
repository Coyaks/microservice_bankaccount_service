package com.skoy.bootcamp_microservices.service;

import com.skoy.bootcamp_microservices.dto.BankAccountDTO;
import com.skoy.bootcamp_microservices.dto.CustomerDTO;
import com.skoy.bootcamp_microservices.dto.GetAvailableBalanceDTO;
import com.skoy.bootcamp_microservices.dto.UpdateBalanceDTO;
import com.skoy.bootcamp_microservices.enums.AccountTypeEnum;
import com.skoy.bootcamp_microservices.enums.CustomerTypeEnum;
import com.skoy.bootcamp_microservices.enums.TransactionTypeEnum;
import com.skoy.bootcamp_microservices.mapper.BankAccountMapper;
import com.skoy.bootcamp_microservices.model.BankAccount;
import com.skoy.bootcamp_microservices.repository.IBankAccountRepository;
import com.skoy.bootcamp_microservices.utils.ApiResponse;
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

@Service
@RequiredArgsConstructor
public class BankAccountService implements IBankAccountService {

    private final IBankAccountRepository repository;
    private final WebClient.Builder webClientBuilder;
    private static final Logger log = LoggerFactory.getLogger(BankAccountService.class);

    @Value("${customer.service.url}")
    private String customerServiceUrl;

    @Override
    public Mono<BankAccountDTO> create(BankAccountDTO bankAccountDTO) {

        return webClientBuilder.build()
                .get()
                .uri(customerServiceUrl + "/customers/" + bankAccountDTO.getCustomerId())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<CustomerDTO>>() {})
                .flatMap(rspCustomer -> {
                    CustomerDTO customer = rspCustomer.getData();
                    if (customer == null) return Mono.error(new RuntimeException("Cliente no encontrado"));

                    // Verificar tipo de cliente y tipo de cuenta

                    if(customer.getCustomerType().equals(CustomerTypeEnum.EMPRESARIAL) &&
                            (bankAccountDTO.getAccountType().equals(AccountTypeEnum.AHORRO) || bankAccountDTO.getAccountType().equals(AccountTypeEnum.PLAZO_FIJO))){
                        return Mono.error(new RuntimeException("Un cliente empresarial no puede tener una cuenta de ahorro o de plazo fijo"));

                    }

                    //check accounts customer
                    return repository.findAllByCustomerId(bankAccountDTO.getCustomerId())
                            .collectList()
                            .flatMap(rspAccounts -> {
                                System.out.println("Existing Accounts: " + rspAccounts);
                                //List<BankAccountDTO> rspAccounts = rspAccounts;
                                if(customer.getCustomerType().equals(CustomerTypeEnum.PERSONAL)){
                                    boolean hasAccountType = rspAccounts.stream().anyMatch(account -> account.getAccountType().equals(bankAccountDTO.getAccountType()));
                                    if (hasAccountType) {
                                        return Mono.error(new RuntimeException("El cliente como maximo puede tener una cuenta de este tipo"));
                                    }
                                }
                                return repository.save(BankAccountMapper.toEntity(bankAccountDTO))
                                        .map(BankAccountMapper::toDto);

                            });
                })
                .doOnError(error -> log.error("Error al validar cliente: {}", error.getMessage()));
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


    @Override
    public Mono<BankAccount> updateBalance(UpdateBalanceDTO updateBalanceDTO) {
        if (updateBalanceDTO.getProductTypeId() == null || updateBalanceDTO.getProductTypeId().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El ID de la cuenta bancaria no puede estar vacío"));
        }

        return repository.findById(updateBalanceDTO.getProductTypeId())
                .flatMap(account -> {
                    BigDecimal newBalance;
                    if (updateBalanceDTO.getTransactionType() == TransactionTypeEnum.DEPOSIT) {
                        newBalance = account.getAvailableBalance().add(updateBalanceDTO.getAmount());
                    } else if (updateBalanceDTO.getTransactionType() == TransactionTypeEnum.WITHDRAWAL) {
                        newBalance = account.getAvailableBalance().subtract(updateBalanceDTO.getAmount());
                    } else {
                        return Mono.error(new IllegalArgumentException("Tipo de transacción no válido"));
                    }
                    account.setAvailableBalance(newBalance);
                    return repository.save(account);
                })
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Cuenta bancaria no encontrada")));
    }

    @Override
    public Mono<BigDecimal> getAvailableBalanceByCustomerId(GetAvailableBalanceDTO getAvailableBalanceDTO) {
        return webClientBuilder.build()
                .get()
                .uri(customerServiceUrl + "/customers/" + getAvailableBalanceDTO.getCustomerId())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<CustomerDTO>>() {})
                .flatMap(response -> {
                    if (response.getData() == null) return Mono.error(new RuntimeException("Cliente no encontrado"));
                    return repository.findAllByCustomerId(getAvailableBalanceDTO.getCustomerId())
                            .filter(account -> account.getAccountType().equals(getAvailableBalanceDTO.getAccountType()))
                            .singleOrEmpty()
                            .map(BankAccountDTO::getAvailableBalance)
                            .switchIfEmpty(Mono.error(new RuntimeException("No se encontró una cuenta con el tipo especificado para el cliente")));
                });
    }

}