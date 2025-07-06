package com.practice.intershop.service.validation;

import com.practice.intershop.model.SalesOrder;

public interface OrderValidationService {
    boolean isValid(SalesOrder order);
}
