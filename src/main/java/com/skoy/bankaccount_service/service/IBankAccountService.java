package com.skoy.bankaccount_service.service;

import com.skoy.bankaccount_service.dto.BankAccountDTO;
import com.skoy.bankaccount_service.dto.GetAvailableBalanceDTO;
import com.skoy.bankaccount_service.dto.TransferDTO;
import com.skoy.bankaccount_service.dto.UpdateBalanceDTO;
import com.skoy.bankaccount_service.model.BankAccountResponse;
import com.skoy.bankaccount_service.utils.ApiResponse;
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

    //Mono<BankAccount> updateBalance(UpdateBalanceDTO updateBalanceDTO);
    Mono<BankAccountResponse> updateBalance(UpdateBalanceDTO updateBalanceDTO);
    Mono<BigDecimal> getAvailableBalanceByCustomerId(GetAvailableBalanceDTO getAvailableBalanceDTO);
    Mono<ApiResponse<Object>> transfer(TransferDTO transferDTO);
}
