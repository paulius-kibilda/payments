package com.payments.controller;

import com.payments.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Test
    void createPaymentType1() throws Exception {
        UUID id = UUID.randomUUID();
        when(paymentService.createPayment(any(), anyString())).thenReturn(id);

        String body = """
                {
                  "type": "TYPE1",
                  "amount": 100,
                  "currency": "EUR",
                  "debtorIban": "LT123",
                  "creditorIban": "LT456",
                  "details": "Test"
                }
                """;

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void createPaymentType2() throws Exception {
        UUID id = UUID.randomUUID();
        when(paymentService.createPayment(any(), anyString())).thenReturn(id);

        String body = """
                {
                  "type": "TYPE2",
                  "amount": 50,
                  "currency": "USD",
                  "debtorIban": "LT123",
                  "creditorIban": "LT456"
                }
                """;

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void createPaymentType3() throws Exception {
        UUID id = UUID.randomUUID();
        when(paymentService.createPayment(any(), anyString())).thenReturn(id);

        String body = """
                {
                  "type": "TYPE3",
                  "amount": 50,
                  "currency": "EUR",
                  "debtorIban": "LT123",
                  "creditorIban": "LT456",
                  "creditorBic": "HBUKGB4B"
                }
                """;

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }
}
