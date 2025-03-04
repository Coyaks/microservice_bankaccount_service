package com.skoy.bootcamp_microservices.repository;

import com.skoy.bootcamp_microservices.dto.BankAccountDTO;
import com.skoy.bootcamp_microservices.model.BankAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface IBankAccountRepository extends ReactiveMongoRepository<BankAccount, String> {
    Flux<BankAccountDTO> findAllByCustomerId(String customerId);
}
