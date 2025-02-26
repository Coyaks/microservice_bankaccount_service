package com.skoy.bootcamp_microservices.service;

import com.skoy.bootcamp_microservices.dto.BankAccountDTO;
import com.skoy.bootcamp_microservices.model.BankAccount;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBankAccountService {

    Mono<BankAccountDTO> create(BankAccountDTO accountDTO);
    Flux<BankAccountDTO> findAll();
    Mono<BankAccountDTO> findById(String id);
    Mono<BankAccountDTO> update(String id, BankAccountDTO accountDTO);
    Mono<Void> delete(String id);
    //Flux<BankAccount> findAllAccountByCustomerId(String customerId);
    Flux<BankAccountDTO> findAllAccountByCustomerId(String customerId);
}
