package com.practice.intershop.service;

import com.practice.intershop.api.client.PaymentApi;
import com.practice.intershop.api.model.ErrorResponse;
import com.practice.intershop.api.model.PaymentRequest;
import com.practice.intershop.api.model.PaymentResponse;
import com.practice.intershop.exception.PaymentFailedException;
import com.practice.intershop.model.SalesOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentApi paymentApi;

    @Override
    public Mono<PaymentResponse> performPayment(SalesOrder order) {
        BigDecimal sum = order.getOrderItems().stream()
                .map(oi -> oi.getUnitPrice().multiply(BigDecimal.valueOf(oi.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        return paymentApi.paymentPost(new PaymentRequest().amount(sum.toString()).userId(1L))
                .onErrorResume(WebClientResponseException.class, ex -> {
                    ErrorResponse errorResponse;
                    try {
                        errorResponse = ex.getResponseBodyAs(ErrorResponse.class);
                    } catch (Exception e) {
                        errorResponse = new ErrorResponse().error("Failed to parse error response: " + ex.getMessage());
                    }
                    return Mono.error(new PaymentFailedException("Payment failed: " + errorResponse.getError()));
                })
                .onErrorResume(Exception.class,
                        ex -> Mono.error(new PaymentFailedException("Unexpected error during payment: " + ex.getMessage())));
    }
}
