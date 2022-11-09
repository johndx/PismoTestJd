package com.pismo.core.accounts.web.rest;


import com.pismo.core.accounts.domain.Accounts;
import com.pismo.core.accounts.domain.OperationsTypes;
import com.pismo.core.accounts.domain.Transactions;
import com.pismo.core.accounts.dtos.TransactionsDto;
import com.pismo.core.accounts.repository.AccountsRepository;
import com.pismo.core.accounts.repository.OperationsTypesRepository;
import com.pismo.core.accounts.repository.TransactionsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * REST controller for managing Transactions.
 * This Controller will receive requests to create a new Customer
 * Account Transaction of the form "<webContext>/transactions"
 */
@RestController
@Transactional
public class TransactionsController {

    private final Logger log = LoggerFactory.getLogger(TransactionsController.class);

    private static final String ENTITY_NAME = "transactions";
    private static final String PAYMENT = "PAYMENT";


    private final AccountsRepository accountsRepository;
    private final OperationsTypesRepository operationsTypesRepository;
    private final TransactionsRepository transactionsRepository;


    /**
     * Contractor with class dependencies for autowiring.
     * @param accountsRepository
     * @param operationsTypesRepository
     * @param transactionsRepository
     */
    public TransactionsController(AccountsRepository accountsRepository,
                                  OperationsTypesRepository operationsTypesRepository,
                                  TransactionsRepository transactionsRepository) {
        this.accountsRepository = accountsRepository;
        this.operationsTypesRepository = operationsTypesRepository;
        this.transactionsRepository = transactionsRepository;
    }

    /**
     * {@code POST  /transactions} : Create a new valid transaction for an account.
     * The incoming DTO will be validated using JSR 303 Hibernate Bean validation.
     *
     * @param transactionsDto the transaction to create and associate with the supplied account.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transaction, or with status {@code 400 (Bad Request)} if the operation fails.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transactions")
    public ResponseEntity<Transactions> createTransaction(@Valid @RequestBody TransactionsDto transactionsDto) throws URISyntaxException {

        log.info("REST request to save Transactions data via Transaction DTO : {}", transactionsDto);

        // Lookup the Account Entity for the incoming Id.
        Optional<Accounts> clientAccountWrapper = accountsRepository.findById(transactionsDto.getAccountId());
        if(!clientAccountWrapper.isPresent()) {
            // No client exists for this Id.
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Account does not exist for id: " + transactionsDto.getAccountId(), null);
        }

        // Lookup the Operations Type Entity for the incoming Operations Id.
        Optional<OperationsTypes> clientOperationsTypes = operationsTypesRepository.findById(transactionsDto.getOperationTypeId());
        if(!clientOperationsTypes.isPresent()) {
            // No Operations Type exists for this Id.
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Operations Type does not exist for id: " + transactionsDto.getOperationTypeId(), null);
        }

        // Now build and persist the Transaction Entity.
        Accounts clientAccount = clientAccountWrapper.get();
        ZonedDateTime now = ZonedDateTime.now();
        Transactions clientTransaction = new Transactions();
        // Sign the Amount correctly based on the Op Type ID
        clientTransaction.setAmount(clientOperationsTypes.get().getDescription().equals(PAYMENT) ?
                                                    transactionsDto.getAmount() :
                                                    transactionsDto.getAmount() * -1);
        clientTransaction.setEventDate(now);
        clientTransaction.setOperationsTypes(clientOperationsTypes.get());
        clientAccount.addTransactions(clientTransaction);
        clientTransaction.setAccounts(clientAccount);
        Transactions persistedTransaction = transactionsRepository.save(clientTransaction);

        if (persistedTransaction == null) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Internal error while processing account Id " + transactionsDto.getAccountId(), null);
        }
        return ResponseEntity
                .created(new URI("/transactions/" + persistedTransaction.getId()))
                .body(persistedTransaction);
    }


}
