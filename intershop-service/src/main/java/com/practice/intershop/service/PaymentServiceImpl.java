package com.practice.intershop.service;

import com.practice.intershop.api.client.PaymentApi;
import com.practice.intershop.api.model.PaymentRequest;
import com.practice.intershop.api.model.PaymentResponse;
import com.practice.intershop.model.SalesOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentApi paymentApi;

    @Override
    public Mono<PaymentResponse> performPayment(SalesOrder order) {
        return paymentApi.paymentPost(new PaymentRequest().amount("10.00").userId(0L));
    }
}
