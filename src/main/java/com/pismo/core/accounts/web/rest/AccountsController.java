package com.pismo.core.accounts.web.rest;


import com.pismo.core.accounts.domain.Accounts;
import com.pismo.core.accounts.repository.AccountsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * REST controller for managing Accounts.
 * Delegates directly to a Repository Tier, as an intermediate 'Service'
 * Tier is not warranted at this stage.
 */
@RestController
@Transactional
public class AccountsController {

    private final Logger log = LoggerFactory.getLogger(AccountsController.class);

    private static final String ENTITY_NAME = "accounts";


    private final AccountsRepository accountsRepository;

    /**
     * Constructor that will autowire in the required dependencies
     * @param accountsRepository
     */
    public AccountsController(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }


    /**
     * {@code POST  /accounts} : Create a new account.
     *
     * @param accounts the account to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accounts, or with status {@code 400 (Bad Request)} if the accounts has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/accounts")
    public ResponseEntity<Accounts> createAccounts(@RequestBody Accounts accounts) throws URISyntaxException {

       log.info("REST request to save Accounts : {}", accounts);
        if (accounts.getId() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Accounts result = accountsRepository.save(accounts);
        return ResponseEntity
                .created(new URI("/accounts/" + result.getId()))
                .body(result);
    }


    /**
     * {@code GET  /accounts/:id} : get the account record with specific "id" .
     *
     * @param id the id of the accounts to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body of the account, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/accounts/{id}")
    public ResponseEntity<Accounts> getAccounts(@PathVariable Long id) {

        log.info("REST request to get Account with Id : {}", id);
        Optional<Accounts> accounts = accountsRepository.findById(id);

        HttpHeaders responseHeaders = new HttpHeaders();
        if (accounts.isPresent()) {
            return new ResponseEntity<>(accounts.get(), responseHeaders, HttpStatus.OK);
        } else {
            // Invalid Account Id Supplied.
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.NOT_FOUND);
        }
    }

}
