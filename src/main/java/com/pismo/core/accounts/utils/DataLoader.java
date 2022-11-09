package com.pismo.core.accounts.utils;

import com.pismo.core.accounts.domain.Accounts;
import com.pismo.core.accounts.domain.OperationsTypes;
import com.pismo.core.accounts.domain.Transactions;
import com.pismo.core.accounts.repository.AccountsRepository;
import com.pismo.core.accounts.repository.OperationsTypesRepository;
import com.pismo.core.accounts.repository.TransactionsRepository;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.*;


/**
 * Data Loader Utility class to initialize the repository with required domain data
 * and sufficient sample data to allow some representative queries to be made.
 */
@Component
@Transactional
public class DataLoader {

    AccountsRepository accountsRepo;
    OperationsTypesRepository operationsRepo;
    TransactionsRepository transactionsRepo;

    private static final List<String> OpDescArray = Arrays.asList("PURCHASE","INSTALLMENT PURCHASE","WITHDRAWAL","PAYMENT");
    private static final String SAMPLE_DOC_NUMBER = "12345678";

    /**
     * Constructor performing Spring autowireing of dependencies.
     * @param accountsRepo
     * @param operationsRepo
     * @param transactionsRepo
     */
    public DataLoader(AccountsRepository accountsRepo, OperationsTypesRepository operationsRepo, TransactionsRepository transactionsRepo) {
        this.accountsRepo = accountsRepo;
        this.operationsRepo = operationsRepo;
        this.transactionsRepo = transactionsRepo;
    }

    /**
     * Load required base data.
     * As this is the first JPA code to run using the sequence generator
     * and no user accessible integration points are available for modifying
     * these table entries, we can use the simple Sequence Generator approach
     * to assign the correct Id's to these entities.
     */
    public   void loadBaseData() {
        OperationsTypes operationsTypes;
        for(String desc: OpDescArray) {
            operationsTypes = new OperationsTypes();
            operationsTypes.setDescription(desc);
            operationsRepo.save(operationsTypes);
        }
    }

    /**
     * Build some sample Test data to be able to evaluate the API.
     */
    public   void loadSampleData() {

        // Create Sample Account
        Accounts accounts = new Accounts();
        accounts.setDocumentNumber(SAMPLE_DOC_NUMBER);
        Accounts savedAccounts = accountsRepo.save(accounts);

        // Create some sample Transactions
        List<OperationsTypes> opTypes = operationsRepo.findAll();

        float txnAmount;
        ZonedDateTime now = ZonedDateTime.now();
        Random randomise = new Random();

        //Iterate over the available Operation Types
        Set<Transactions> txnSet = new HashSet<>();
         for(OperationsTypes opType: opTypes) {
            Transactions transactions =  new Transactions();
            transactions.setAccounts(savedAccounts);
            transactions.setOperationsTypes(opType);
            transactions.setEventDate(now);
            transactions.setAmount(opType.getDescription().equals("PAYMENT") ? 40.45F : randomise.nextFloat() * -100);
            transactionsRepo.save(transactions);
            txnSet.add(transactions);
        }
        savedAccounts.setTransactions(txnSet);
        accountsRepo.save(savedAccounts);
        transactionsRepo.flush();
    }


}
