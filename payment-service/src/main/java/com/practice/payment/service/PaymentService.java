package com.practice.payment.service;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface PaymentService {
    Mono<Boolean> performPayment(Long userId, BigDecimal amount);
    Mono<BigDecimal> getBalance(Long userId);
}
