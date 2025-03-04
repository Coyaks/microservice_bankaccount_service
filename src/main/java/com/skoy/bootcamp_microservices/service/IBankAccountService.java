package com.skoy.bootcamp_microservices.service;

import com.skoy.bootcamp_microservices.dto.BankAccountDTO;
import com.skoy.bootcamp_microservices.dto.GetAvailableBalanceDTO;
import com.skoy.bootcamp_microservices.dto.UpdateBalanceDTO;
import com.skoy.bootcamp_microservices.enums.AccountTypeEnum;
import com.skoy.bootcamp_microservices.model.BankAccount;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface IBankAccountService {

    Mono<BankAccountDTO> create(BankAccountDTO accountDTO);
    Flux<BankAccountDTO> findAll();
    Mono<BankAccountDTO> findById(String id);
    Mono<BankAccountDTO> update(String id, BankAccountDTO accountDTO);
    Mono<Void> delete(String id);
    Flux<BankAccountDTO> findAllByCustomerId(String customerId);

    Mono<BankAccount> updateBalance(UpdateBalanceDTO updateBalanceDTO);
    Mono<BigDecimal> getAvailableBalanceByCustomerId(GetAvailableBalanceDTO getAvailableBalanceDTO);
}
