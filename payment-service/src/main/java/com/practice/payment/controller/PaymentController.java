package com.practice.payment.controller;


import com.practice.api.BalanceApi;
import com.practice.api.PaymentApi;
import com.practice.dto.BalanceResponse;
import com.practice.dto.ErrorResponse;
import com.practice.dto.PaymentRequest;
import com.practice.dto.PaymentResponse;
import com.practice.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class PaymentController implements PaymentApi, BalanceApi {

    private final PaymentService paymentService;

    @Override
    @GetMapping("/balance/{userId}")
    public Mono<ResponseEntity<BalanceResponse>> balanceUserIdGet(@PathVariable("userId") Long userId,
                                                                  ServerWebExchange exchange) {
        return paymentService.getBalance(userId)
                .map(balance -> balance.setScale(2, RoundingMode.HALF_UP))
                .map(balance -> ResponseEntity.ok(
                        new BalanceResponse().userId(userId).balance(balance.toString()))
                );
    }

    @Override
    @PostMapping("/payment")
    public Mono<ResponseEntity<PaymentResponse>> paymentPost(@RequestBody Mono<PaymentRequest> paymentRequest,
                                                             ServerWebExchange exchange) {
        return paymentRequest
                .flatMap(req -> {
                    Long userId = req.getUserId();
                    return paymentService.performPayment(userId, new BigDecimal(req.getAmount()))
                            .flatMap(success -> paymentService.getBalance(userId)
                                    .map(balance -> balance.setScale(2, RoundingMode.HALF_UP))
                                    .map(balance -> ResponseEntity.ok(
                                            new PaymentResponse().newBalance(balance.toString()).success(success)
                                    )));
                });
    }

    @ExceptionHandler(NoSuchElementException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleNotFound(NoSuchElementException ex) {
        return Mono.just(
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse().error(ex.getMessage()))
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleIllegalState(IllegalStateException ex) {
        return Mono.just(
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse().error(ex.getMessage()))
        );
    }
}
