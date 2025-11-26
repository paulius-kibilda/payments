package com.payments.service;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentServiceFlowIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Test
    void createThenGetPayments() throws Exception {

        String createBody = """
                {
                  "type": "TYPE1",
                  "amount": 50,
                  "currency": "EUR",
                  "debtorIban": "LT123",
                  "creditorIban": "LT456",
                  "details": "test"
                }
                """;

        MvcResult r1 = mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated())
                .andReturn();

        String id1 = JsonPath.read(r1.getResponse().getContentAsString(), "$.id");

        createBody = """
                {
                  "type": "TYPE2",
                  "amount": 50,
                  "currency": "USD",
                  "debtorIban": "LT123",
                  "creditorIban": "LT456"
                }
                """;

        MvcResult r2 = mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated())
                .andReturn();

        String id2 = JsonPath.read(r2.getResponse().getContentAsString(), "$.id");

        createBody = """
                {
                  "type": "TYPE3",
                  "amount": 50,
                  "currency": "EUR",
                  "debtorIban": "LT123",
                  "creditorIban": "LT456",
                  "creditorBic": "HBUKGB4B"
                }
                """;

        MvcResult r3 = mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated())
                .andReturn();

        String id3 = JsonPath.read(r3.getResponse().getContentAsString(), "$.id");

        MvcResult getResult = mockMvc.perform(get("/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ids").isArray())
                .andReturn();

        String listJson = getResult.getResponse().getContentAsString();
        List<String> ids = JsonPath.read(listJson, "$.ids");

        assertTrue(ids.contains(id1));
        assertTrue(ids.contains(id2));
        assertTrue(ids.contains(id3));
    }


    @Test
    void createThenGetPaymentType1() throws Exception {
        String createBody = """
                {
                  "type": "TYPE1",
                  "amount": 50,
                  "currency": "EUR",
                  "debtorIban": "LT123",
                  "creditorIban": "LT456",
                  "details": "test"
                }
                """;

        MvcResult result = mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String id = JsonPath.read(response, "$.id");

        mockMvc.perform(get("/payments/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void createThenCancelPaymentType1() throws Exception {
        String createBody = """
                {
                  "type": "TYPE1",
                  "amount": 50,
                  "currency": "EUR",
                  "debtorIban": "LT123",
                  "creditorIban": "LT456",
                  "details": "test"
                }
                """;

        MvcResult result = mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        String id = JsonPath.read(responseJson, "$.id");

        mockMvc.perform(post("/payments/{id}/cancel", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/payments/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.canceled").value(true));
    }

    @Test
    void createThenGetPaymentType2() throws Exception {
        String createBody = """
                {
                  "type": "TYPE2",
                  "amount": 50,
                  "currency": "USD",
                  "debtorIban": "LT123",
                  "creditorIban": "LT456"
                }
                """;

        MvcResult result = mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String id = JsonPath.read(response, "$.id");

        mockMvc.perform(get("/payments/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void createThenCancelPaymentType2() throws Exception {
        String createBody = """
                {
                  "type": "TYPE2",
                  "amount": 50,
                  "currency": "USD",
                  "debtorIban": "LT123",
                  "creditorIban": "LT456"
                }
                """;

        MvcResult result = mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        String id = JsonPath.read(responseJson, "$.id");

        mockMvc.perform(post("/payments/{id}/cancel", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/payments/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.canceled").value(true));
    }

    @Test
    void createThenGetPaymentType3() throws Exception {
        String createBody = """
                {
                  "type": "TYPE3",
                  "amount": 50,
                  "currency": "EUR",
                  "debtorIban": "LT123",
                  "creditorIban": "LT456",
                  "creditorBic": "HBUKGB4B"
                }
                """;

        MvcResult result = mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String id = JsonPath.read(response, "$.id");

        mockMvc.perform(get("/payments/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void createThenCancelPaymentType3() throws Exception {
        String createBody = """
                {
                  "type": "TYPE3",
                  "amount": 50,
                  "currency": "EUR",
                  "debtorIban": "LT123",
                  "creditorIban": "LT456",
                  "creditorBic": "HBUKGB4B"
                }
                """;

        MvcResult result = mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        String id = JsonPath.read(responseJson, "$.id");

        mockMvc.perform(post("/payments/{id}/cancel", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/payments/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.canceled").value(true));
    }
}
