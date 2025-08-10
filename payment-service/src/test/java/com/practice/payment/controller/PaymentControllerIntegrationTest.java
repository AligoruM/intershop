package com.practice.payment.controller;


import com.practice.dto.BalanceResponse;
import com.practice.dto.ErrorResponse;
import com.practice.dto.PaymentRequest;
import com.practice.dto.PaymentResponse;
import com.practice.payment.model.UserBalance;
import com.practice.payment.repository.UserBalanceR2dbcRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public class PaymentControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private UserBalanceR2dbcRepository userBalanceR2dbcRepository;

    @BeforeEach
    void setUp() {
        userBalanceR2dbcRepository.deleteAll().block();
    }

    @Test
    void getBalance_shouldReturnBalance_whenUserExists() {
        Long userId = 1L;
        BigDecimal balance = new BigDecimal("100.50");
        UserBalance userBalance = new UserBalance();
        userBalance.setUserId(userId);
        userBalance.setBalance(balance);
        userBalanceR2dbcRepository.save(userBalance).block();

        webTestClient.get()
                .uri("/balance/{userId}", userId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BalanceResponse.class)
                .value(response -> {
                    assertThat(response.getUserId()).isEqualTo(userId);
                    assertThat(response.getBalance()).isEqualTo(balance.toPlainString());
                });
    }

    @Test
    void getBalance_shouldReturn404_whenUserNotFound() {
        Long userId = 999L;

        webTestClient.get()
                .uri("/balance/{userId}", userId)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(response -> assertThat(response.getError()).isEqualTo("User not found"));
    }

    @Test
    void performPayment_shouldProcessPayment_whenEnoughMoney() {
        Long userId = 1L;
        BigDecimal initialBalance = new BigDecimal("150.00");
        BigDecimal paymentAmount = new BigDecimal("50.00");
        BigDecimal expectedBalance = new BigDecimal("100.00");

        UserBalance userBalance = new UserBalance();
        userBalance.setUserId(userId);
        userBalance.setBalance(initialBalance);
        userBalanceR2dbcRepository.save(userBalance).block();

        PaymentRequest request = new PaymentRequest()
                .userId(userId)
                .amount(paymentAmount.toString());


        // Act & Assert
        webTestClient.post()
                .uri("/payment")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaymentResponse.class)
                .value(response -> {
                    assertThat(response.getSuccess()).isTrue();
                    assertThat(response.getNewBalance()).isEqualTo(expectedBalance.toPlainString());
                });
    }

    @Test
    void performPayment_shouldReturn400_whenNotEnoughMoney() {
        Long userId = 1L;
        BigDecimal initialBalance = new BigDecimal("30.00");
        BigDecimal paymentAmount = new BigDecimal("50.00");

        UserBalance userBalance = new UserBalance();
        userBalance.setUserId(userId);
        userBalance.setBalance(initialBalance);

        userBalanceR2dbcRepository.save(userBalance).block();

        PaymentRequest request = new PaymentRequest()
                .userId(userId)
                .amount(paymentAmount.toString());


        webTestClient.post()
                .uri("/payment")
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(response -> assertThat(response.getError()).isEqualTo("Not enough money"));
    }

    @Test
    void performPayment_shouldReturn404_whenUserNotFound() {
        Long userId = 999L;
        PaymentRequest request = new PaymentRequest()
                .userId(userId)
                .amount("10.00");

        webTestClient.post()
                .uri("/payment")
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(response -> assertThat(response.getError()).isEqualTo("User not found"));
    }
}