package com.skoy.bankaccount_service.controller;

import com.skoy.bankaccount_service.dto.BankAccountDTO;
import com.skoy.bankaccount_service.dto.GetAvailableBalanceDTO;
import com.skoy.bankaccount_service.dto.TransferDTO;
import com.skoy.bankaccount_service.dto.UpdateBalanceDTO;
import com.skoy.bankaccount_service.enums.AccountTypeEnum;
import com.skoy.bankaccount_service.model.BankAccount;
import com.skoy.bankaccount_service.service.IBankAccountService;
import com.skoy.bankaccount_service.utils.ApiResponse;
import com.skoy.bankaccount_service.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;


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
    public Mono<ApiResponse<BankAccountDTO>> findById(@PathVariable String id) {
        logger.info("Fetching bank_accounts with ID: {}", id);
        return service.findById(id)
                .map(customer -> {
                    logger.info("bank_accounts found: {}", customer);
                    return new ApiResponse<>("Cliente encontrado", customer, Constants.STATUS_OK);
                })
                .switchIfEmpty(Mono.just(new ApiResponse<>("Cliente no encontrado", null, Constants.STATUS_E404)))
                .doOnError(e -> logger.error("Error fetching bank_accounts with ID: {}", id, e));
    }


    @PostMapping
    public Mono<ApiResponse<BankAccountDTO>> create(@RequestBody BankAccountDTO bankAccountDto) {
        logger.info("Creating new bank_accounts: {}", bankAccountDto);
        return service.create(bankAccountDto)
                .map(createdItem -> {
                    if (createdItem != null) {
                        logger.info("bank_accounts created successfully: {}", createdItem);
                        return new ApiResponse<>("ok", createdItem, Constants.STATUS_OK);
                    } else {
                        logger.error("Error creating bank_accounts");
                        return new ApiResponse<>("error", null, Constants.STATUS_E500);
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
                            return new ApiResponse<>("Actualizado correctamente", updatedCustomer, Constants.STATUS_OK);
                        }))
                .switchIfEmpty(Mono.just(new ApiResponse<>("ID no encontrado", null, Constants.STATUS_E404)))
                .doOnError(e -> logger.error("Error updating bank_accounts with ID: {}", id, e));
    }

    @DeleteMapping("/{id}")
    public Mono<ApiResponse<Void>> delete(@PathVariable String id) {
        logger.info("Deleting bank_accounts with ID: {}", id);
        return service.findById(id)
                .flatMap(existingCustomer -> service.delete(id)
                        .then(Mono.just(new ApiResponse<Void>("Eliminado correctamente", null, Constants.STATUS_OK))))
                .switchIfEmpty(Mono.just(new ApiResponse<Void>("ID no encontrado", null, Constants.STATUS_E404)))
                .onErrorResume(e -> {
                    logger.error("Error deleting bank_accounts with ID: {}", id, e);
                    return Mono.just(new ApiResponse<Void>("Error al eliminar", null, Constants.STATUS_E500));
                });
    }

    @GetMapping("/types")
    public AccountTypeEnum[] getAllBankAccountTypes() {
        return AccountTypeEnum.values();
    }

    @GetMapping("/customer/{customerId}")
    public Flux<BankAccountDTO> findAllByCustomerId(@PathVariable String customerId) {
        logger.info("Fetching all bank_accounts by customer ID: {}", customerId);
        return service.findAllByCustomerId(customerId);
    }

    @PostMapping("/update_balance")
    public Mono<ApiResponse<BankAccount>> updateBalance(@RequestBody UpdateBalanceDTO updateBalanceDTO) {
        logger.info("Updating balance for bank account ID: {}", updateBalanceDTO.getProductTypeId());
        return service.updateBalance(updateBalanceDTO)
                .map(updatedAccount -> new ApiResponse<>("Balance actualizado correctamente", updatedAccount.getBankAccount(), Constants.STATUS_OK, updatedAccount.getCommissionAmount()))
                .doOnError(e -> logger.error("Error updating balance for bank account ID: {}", updateBalanceDTO.getProductTypeId(), e));
    }

    @PostMapping("/check_available_balance")
    public Mono<ApiResponse<BigDecimal>> getAvailableBalanceByCustomerId(@RequestBody GetAvailableBalanceDTO getAvailableBalanceDTO) {
        return service.getAvailableBalanceByCustomerId(getAvailableBalanceDTO)
                .map(balance -> new ApiResponse<>("Saldo disponible encontrado", balance, Constants.STATUS_OK))
                .doOnError(e -> logger.error("Error fetching available balance for customer ID: {} and account type: {}", getAvailableBalanceDTO.getCustomerId(), getAvailableBalanceDTO.getAccountType(), e));
    }

    @PostMapping("/transfer")
    public Mono<ApiResponse<Object>> transfer(@RequestBody TransferDTO transferDTO) {
        logger.info("Transferring amount: {} from account ID: {} to account ID: {}", transferDTO.getAmount(), transferDTO.getSourceAccountId(), transferDTO.getDestinationAccountId());
        return service.transfer(transferDTO)
                .doOnError(e -> logger.error("Error transferring amount: {} from account ID: {} to account ID: {}", transferDTO.getAmount(), transferDTO.getSourceAccountId(), transferDTO.getDestinationAccountId(), e));
    }

}
