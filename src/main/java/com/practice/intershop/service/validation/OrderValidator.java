package com.practice.intershop.service.validation;

import com.practice.intershop.exception.IntershopCustomException;
import com.practice.intershop.model.SalesOrder;

public interface OrderValidator {

    boolean isValid(SalesOrder order);

    IntershopCustomException getException();
}
