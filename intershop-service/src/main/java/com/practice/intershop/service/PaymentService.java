package com.practice.intershop.service;

import com.practice.intershop.api.model.PaymentResponse;
import com.practice.intershop.model.SalesOrder;
import reactor.core.publisher.Mono;

public interface PaymentService {
    Mono<PaymentResponse> performPayment(SalesOrder order);
}
