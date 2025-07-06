package com.practice.intershop.service.validation;

import com.practice.intershop.enums.OrderStatus;
import com.practice.intershop.exception.IntershopCustomException;
import com.practice.intershop.model.SalesOrder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class OrderPlannedValidatorImpl implements OrderValidator {

    @Override
    public boolean isValid(SalesOrder order) {
        return order != null && order.getOrderStatus() == OrderStatus.PLANNED;
    }

    @Override
    public IntershopCustomException getException() {
        return new IntershopCustomException("Order not in planned status");
    }
}
