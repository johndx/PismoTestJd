package com.pismo.core.accounts.web.rest;

import com.pismo.core.accounts.domain.Accounts;
import com.pismo.core.accounts.dtos.TransactionsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionsControllerTest extends ControllerBaseTests {

    private final String BASE_URL = "/transactions";

    /*   Test Data   */
    private final float AMOUNT = 34.56F;
    private final long OP_TYPE_ID = 3L;

    private Accounts existingAccount;


    @BeforeEach
    public void initTest() throws Exception {
        this.existingAccount = getNewPersistedAccountDetails();
    }



    /**
     * Verify that as basic POST with document data succeeds, using
     * Mock MVC to emulate a REST call.
     * @throws Exception
     */
    @Test
    @Transactional
    public void mockValidTransactionsPOSTShouldPass() throws Exception {

        String url = BASE_URL;
        long accountId = existingAccount.getId();
        TransactionsDto transactionsDto = new TransactionsDto(accountId, OP_TYPE_ID, AMOUNT);
        MvcResult result  = getMockMvc().perform(post(url).contentType(APPLICATION_JSON)
                                        .content(ObjectToJsonConverter(transactionsDto)))
                                        .andExpect(status().isCreated())
                                        .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        assertThat(responseContent.toUpperCase().contains(Float.toString(AMOUNT)));
        // Unlikely to have another instance of this float value in the String.
    }

    /**
     * Verify that as POST to the /transactions endpoint with an invalid
     * Account Id will fail with status 400 - Bad Request.
     * Uses Mock MVC to emulate a REST call.
     * @throws Exception
     */
    @Test
    @Transactional
    public void mockInvalidActIdTransactionsPOSTShouldFail() throws Exception {

        String url = BASE_URL;
        long accountId = 99l;
        TransactionsDto transactionsDto = new TransactionsDto(accountId, OP_TYPE_ID, AMOUNT);
        getMockMvc().perform(post(url).contentType(APPLICATION_JSON)
                        .content(ObjectToJsonConverter(transactionsDto)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Verify that as POST to the /transactions endpoint with an invalid
     * Operation Type Id will fail with status 400 - Bad Request.
     * Uses Mock MVC to emulate a REST call.
     * @throws Exception
     */
    @Test
    @Transactional
    public void mockInvalidOpTypeIdTransactionsPOSTShouldFail() throws Exception {

        String url = BASE_URL;
        long accountId = existingAccount.getId();
        TransactionsDto transactionsDto = new TransactionsDto(accountId, 999L, AMOUNT);
        getMockMvc().perform(post(url).contentType(APPLICATION_JSON)
                        .content(ObjectToJsonConverter(transactionsDto)))
                .andExpect(status().isBadRequest());
    }

}