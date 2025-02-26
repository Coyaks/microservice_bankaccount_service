package com.skoy.bootcamp_microservices.service;

import com.skoy.bootcamp_microservices.dto.BankAccountDTO;
import com.skoy.bootcamp_microservices.dto.CustomerDTO;
import com.skoy.bootcamp_microservices.enums.AccountTypeEnum;
import com.skoy.bootcamp_microservices.enums.CustomerTypeEnum;
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

import java.time.LocalDateTime;
import java.util.List;

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
                    return repository.findAllAccountByCustomerId(bankAccountDTO.getCustomerId())
                            .collectList()
                            .flatMap(rspAccounts -> {
                                System.out.println("Existing Accounts: " + rspAccounts);
                                //List<BankAccountDTO> rspAccounts = rspAccounts;
                                if(customer.getCustomerType().equals(CustomerTypeEnum.PERSONAL)){
                                    boolean hasAccountType = rspAccounts.stream().anyMatch(account -> account.getAccountType().equals(bankAccountDTO.getAccountType()));
                                    if (hasAccountType) {
                                        return Mono.error(new RuntimeException("El cliente como maximo puede tener una cuenta de este tipo"));
                                    }
                                   /* return repository.save(BankAccountMapper.toEntity(bankAccountDTO))
                                            .map(BankAccountMapper::toDto);*/
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
                .flatMap(existing -> {
                    existing.setAccountType(accountDTO.getAccountType());
                    existing.setBalance(accountDTO.getBalance());
                    existing.setUpdatedAt(LocalDateTime.now());
                    return repository.save(existing);
                })
                .map(BankAccountMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        return repository.deleteById(id);
    }

    @Override
    public Flux<BankAccountDTO> findAllAccountByCustomerId(String customerId) {
        return repository.findAllAccountByCustomerId(customerId);

    }

}