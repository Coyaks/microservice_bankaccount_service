package com.skoy.bootcamp_microservices.controller;

import com.skoy.bootcamp_microservices.dto.BankAccountDTO;
import com.skoy.bootcamp_microservices.service.IBankAccountService;
import com.skoy.bootcamp_microservices.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/v1/bank_accounts")
public class BankAccountController {

    private static final Logger logger = LoggerFactory.getLogger(BankAccountController.class);

    @Autowired
    private IBankAccountService service;

    @GetMapping
    public Flux<BankAccountDTO> findAll() {
        logger.info("Fetching all bank_accounts");

        return service.findAll()
                .doOnNext(customer -> logger.info("bank_accounts found: {}", customer))
                .doOnComplete(() -> logger.info("All bank_accounts fetched successfully."));
    }

    @GetMapping("/{id}")
    public Mono<BankAccountDTO> findById(@PathVariable String id) {
        logger.info("Fetching bank_accounts with ID: {}", id);
        return service.findById(id)
                .doOnNext(customer -> logger.info("bank_accounts found: {}", customer))
                .doOnError(e -> logger.error("Error fetching bank_accounts with ID: {}", id, e));
    }


    @PostMapping
    public Mono<ApiResponse<BankAccountDTO>> create(@RequestBody BankAccountDTO bankAccountDto) {
        logger.info("Creating new bank_accounts: {}", bankAccountDto);
        return service.create(bankAccountDto)
                .map(createdItem -> {
                    if (createdItem != null) {
                        logger.info("bank_accounts created successfully: {}", createdItem);
                        return new ApiResponse<>("ok", createdItem, 200);
                    } else {
                        logger.error("Error creating bank_accounts");
                        return new ApiResponse<>("error", null, 500);
                    }
                });
    }

    @PutMapping("/{id}")
    public Mono<ApiResponse<BankAccountDTO>> update(@PathVariable String id, @RequestBody BankAccountDTO bankAccountDto) {
        logger.info("Updating bank_accounts with ID: {}", id);
        return service.findById(id)
                .flatMap(existingCustomer -> service.update(id, bankAccountDto)
                        .map(updatedCustomer -> {
                            logger.info("bank_accounts updated successfully: {}", updatedCustomer);
                            return new ApiResponse<>("Actualizado correctamente", updatedCustomer, 200);
                        }))
                .switchIfEmpty(Mono.just(new ApiResponse<>("ID no encontrado", null, 404)))
                .doOnError(e -> logger.error("Error updating bank_accounts with ID: {}", id, e));
    }

    @DeleteMapping("/{id}")
    public Mono<ApiResponse<Void>> delete(@PathVariable String id) {
        logger.info("Deleting bank_accounts with ID: {}", id);
        return service.findById(id)
                .flatMap(existingCustomer -> service.delete(id)
                        .then(Mono.just(new ApiResponse<Void>("Eliminado correctamente", null, 200))))
                .switchIfEmpty(Mono.just(new ApiResponse<Void>("ID no encontrado", null, 404)))
                .onErrorResume(e -> {
                    logger.error("Error deleting bank_accounts with ID: {}", id, e);
                    return Mono.just(new ApiResponse<Void>("Error al eliminar", null, 500));
                });
    }


}
