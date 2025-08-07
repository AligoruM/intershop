package com.practice.intershop.service.validation;

import com.practice.intershop.exception.IntershopCustomException;
import com.practice.intershop.model.SalesOrder;
import reactor.core.publisher.Mono;

public interface OrderValidator {

    boolean isValid(SalesOrder order);

    default Mono<Void> validate(SalesOrder order) {
        boolean valid = isValid(order);
        if (!valid) {
            return Mono.error(this::getException);
        }
        return Mono.empty();
    }

    IntershopCustomException getException();
}
