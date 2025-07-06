package com.practice.intershop.service.validation;

import com.practice.intershop.exception.IntershopCustomException;
import com.practice.intershop.model.SalesOrder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class OrderNotEmptyValidatorImpl implements OrderValidator {

    @Override
    public boolean isValid(SalesOrder order) {
        return order != null && order.getOrderItems() != null && !order.getOrderItems().isEmpty();
    }

    @Override
    public IntershopCustomException getException() {
        return new IntershopCustomException("Order is empty");
    }
}
