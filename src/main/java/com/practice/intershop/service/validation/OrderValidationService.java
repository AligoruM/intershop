package com.practice.intershop.service.validation;

import com.practice.intershop.model.SalesOrder;
import reactor.core.publisher.Mono;

public interface OrderValidationService {
    Mono<Void> isValid(SalesOrder order);
}
