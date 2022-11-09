package com.pismo.core.accounts.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pismo.core.accounts.domain.Accounts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Abstract base class for holding shared functionality
 * across common tests classes.
 */
public abstract class ControllerBaseTests {

    private final String ACCOUNTS_BASE_URL = "/accounts";
    protected long BASE_DOCUMENT_ID = 9994499933L;

    @Autowired
    private MockMvc mockMvc;

    /* Define at object level as expensive to construct */
    private ObjectMapper mapper = new ObjectMapper();


    public MockMvc getMockMvc() {
        return mockMvc;
    }

    /*
     * Jackson JSON Mapping
     */
    protected String ObjectToJsonConverter(final Object pojoObject) throws Exception {

        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(pojoObject);
        return requestJson;
    }

    /*
     * Jackson Object Mapping - convert a source Json object representation
     * into a target object, returned as a base Java object.
     */
    protected  Object JsonToObjectConverter(final String sourceJson, final Class targetClass) throws Exception {

        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        Object targetObject = mapper.readValue(sourceJson, targetClass);
        return targetObject;
    }

    /*
     * Create a new persisted account in the repository that can then be
     * used as a valid account in subsequent REST requests.
     */
    protected Accounts getNewPersistedAccountDetails() throws Exception {

        String url = ACCOUNTS_BASE_URL;
        Accounts accounts = new Accounts();
        accounts.setDocumentNumber(Long.toString(BASE_DOCUMENT_ID++));
        MvcResult result = mockMvc.perform(post(url).contentType(APPLICATION_JSON)
                        .content(ObjectToJsonConverter(accounts)))
                .andExpect(status().isCreated())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        Accounts persistedAccount = (Accounts) JsonToObjectConverter(responseContent, Accounts.class);
        return persistedAccount;
    }


}
