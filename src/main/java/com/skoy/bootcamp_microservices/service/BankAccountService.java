package com.skoy.bootcamp_microservices.service;

import com.skoy.bootcamp_microservices.dto.BankAccountDTO;
import com.skoy.bootcamp_microservices.dto.CustomerDTO;
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

@Service
@RequiredArgsConstructor
public class BankAccountService implements IBankAccountService {

    private final IBankAccountRepository repository;
    private final WebClient.Builder webClientBuilder;
    private static final Logger log = LoggerFactory.getLogger(BankAccountService.class);

    @Value("${customer.service.url}")
    private String customerServiceUrl;

    @Override
    public Mono<BankAccountDTO> create(BankAccountDTO accountDTO) {

        return webClientBuilder.build()
                .get()
                .uri(customerServiceUrl + "/customers/" + accountDTO.getCustomerId())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<CustomerDTO>>() {})
                .flatMap(response -> {
                    CustomerDTO customer = response.getData();
                    if (customer != null) {
                        return repository.save(accountDTO.toEntity())
                                .map(BankAccountDTO::fromEntity);
                    } else {
                        return Mono.error(new RuntimeException("Cliente no encontrado"));
                    }
                })
                .doOnError(error -> log.error("Error al validar cliente: {}", error.getMessage()));
    }

    @Override
    public Flux<BankAccountDTO> findAll() {
        return repository.findAll()
                .map(this::convertToDTO);
    }

    @Override
    public Mono<BankAccountDTO> findById(String id) {
        return repository.findById(id)
                .map(this::convertToDTO);
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
                .map(this::convertToDTO);
    }

    @Override
    public Mono<Void> delete(String id) {
        return repository.deleteById(id);
    }

    private BankAccountDTO convertToDTO(BankAccount account) {
        BankAccountDTO dto = new BankAccountDTO();
        dto.setId(account.getId());
        dto.setCustomerId(account.getCustomerId());
        dto.setAccountType(account.getAccountType());
        dto.setBalance(account.getBalance());
        return dto;
    }
}