package com.playtomic.tests.wallet.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * This test is failing with the current implementation.
 *
 * How would you test this?
 */

@ActiveProfiles("test")
@SpringBootTest
public class StripeServiceTest {
    private MockRestServiceServer mockRestServiceServer;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    private StripeService stripeService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("${stripe.simulator.charges-uri}")
    private URI charges;

    @Value("${stripe.simulator.refunds-uri}")
    private URI refunds;


    @BeforeEach
    public void setUp() {
        RestTemplate restTemplate = restTemplateBuilder
                .errorHandler(new StripeRestTemplateResponseErrorHandler())
                .build();
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
        stripeService = new StripeService(
                charges,
                refunds,
                restTemplateBuilder
        );

        stripeService.setRestTemplate(restTemplate);
    }

    @Test
    public void test_exception() {
        mockRestServiceServer.expect(once(), requestTo(charges))
                .andRespond(withStatus(UNPROCESSABLE_ENTITY).contentType(MediaType.APPLICATION_JSON));

        assertThrows(StripeAmountTooSmallException.class, () -> {
            stripeService.charge("4242424242424242", new BigDecimal("5.00"));
        });

        mockRestServiceServer.verify();

    }

    @Test
    public void test_ok() throws JsonProcessingException {
        Payment expectedPayment = new Payment("test_payment_id");
        String paymentJson = objectMapper.writeValueAsString(expectedPayment);
        mockRestServiceServer.expect(once(), requestTo(charges))
                .andRespond(withStatus(OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(paymentJson));

        Payment actualPayment = stripeService.charge("4242424242424242", new BigDecimal("15.00"));
        assertEquals(expectedPayment.getId(), actualPayment.getId());


        mockRestServiceServer.verify();
    }

}
