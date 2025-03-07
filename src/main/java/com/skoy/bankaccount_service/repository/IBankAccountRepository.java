package com.skoy.bankaccount_service.repository;

import com.skoy.bankaccount_service.dto.BankAccountDTO;
import com.skoy.bankaccount_service.model.BankAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface IBankAccountRepository extends ReactiveMongoRepository<BankAccount, String> {
    Flux<BankAccountDTO> findAllByCustomerId(String customerId);
}
