package com.pismo.core.accounts.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.pismo.core.accounts.domain.Accounts;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;


@SpringBootTest
@AutoConfigureMockMvc
public class AccountsControllerTest extends ControllerBaseTests {

    private final String BASE_URL = "/accounts";


    @PostConstruct
    public void initTest() {
        // Segregate document numbers for this test
        BASE_DOCUMENT_ID +=100;
    }

    /**
     * Verify that a basic POST to /accounts with document data succeeds,
     * using Mock MVC to emulate a REST call.
     * @throws Exception
     */
    @Test
    @Transactional
    public void mockValidAccountPOSTShouldPass() throws Exception {

        String url = BASE_URL;
        Accounts accounts = new Accounts();
        accounts.setDocumentNumber(Long.toString(BASE_DOCUMENT_ID++));
        getMockMvc().perform(post(url).contentType(APPLICATION_JSON)
                        .content(ObjectToJsonConverter(accounts)))
                .andExpect(status().isCreated());
    }


    /**
     * Verify that as basic POST to /accounts with document data succeeds,
     * and that a valid Accounts object is returned and that a redirect
     * to the 'Location' in the header will get the newly created object.
     * Using Mock MVC to emulate a REST call.
     * @throws Exception
     */
    @Test
    @Transactional
    public void mockValidAccountPOSTCreatedShouldPass() throws Exception {

        String url = BASE_URL;
        Accounts accounts = new Accounts();
        long testDocId = ++BASE_DOCUMENT_ID;
        accounts.setDocumentNumber(Long.toString(testDocId));
        MvcResult result  =  getMockMvc().perform(post(url).contentType(APPLICATION_JSON)
                                        .content(ObjectToJsonConverter(accounts)))
                                        .andExpect(status().isCreated())
                                        .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        Accounts persistedAcct = (Accounts) JsonToObjectConverter(responseContent, Accounts.class);
        assertThat(persistedAcct.getDocumentNumber()).isEqualTo(Long.toString(testDocId));

        List<String> locations = result.getResponse().getHeaders("Location");
        url = locations.get(0);
        getMockMvc().perform(get(url))
                .andExpect(status().isOk());
    }

    /**
     * Verify that as basic POST to /accounts with an invalid document
     * data fails.
     * Using Mock MVC to emulate a REST call.
     * @throws Exception
     */
    @Test
    public void mockInvalidAccountPOSTShouldFail() throws Exception {

        String url = BASE_URL;
        Accounts accounts = new Accounts();
        accounts.setDocumentNumber(Long.toString(BASE_DOCUMENT_ID++));
        accounts.setId(99L);

        getMockMvc().perform(post(url).contentType(APPLICATION_JSON)
                        .content(ObjectToJsonConverter(accounts)))
                .andExpect(status().isBadRequest());
    }



    /**
     * Verify that as basic GET to /accounts with an invalid Id fails.
     * Using Mock MVC to emulate a REST call.
     * @throws Exception
     */
    @Test
    public void mockInvalidGetAccountsByIdShouldFail() throws Exception {

        String url = BASE_URL+ "/99";
        getMockMvc().perform(get(url))
                .andExpect(status().isNotFound());
    }

    /**
     * Verify that as basic GET request to /accounts with a valid Id  Passes.
     * Using Mock MVC to emulate a REST call.
     * @throws Exception
     */
    @Test
    public void mockValidGetAccountsByIdShouldPass() throws Exception {

        // Get the Id of a newly persisted Account
        Accounts existingAccount = getNewPersistedAccountDetails();
        String url = BASE_URL + "/" + existingAccount.getId();
        getMockMvc().perform(get(url))
                .andExpect(status().isOk());
    }

}