package com.practice.payment.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Override
    public Mono<Boolean> performPayment(Long userId, BigDecimal amount) {
        return Mono.just(true);
    }

    @Override
    public Mono<BigDecimal> getBalance(Long userId) {
        return Mono.just(BigDecimal.ZERO);
    }
}
