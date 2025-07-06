package com.practice.intershop.service.validation;

import com.practice.intershop.model.SalesOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderValidationServiceImpl implements OrderValidationService {

    private final List<OrderValidator> orderValidatorList;

    @Override
    public boolean isValid(SalesOrder order) {
        for (OrderValidator orderValidator : orderValidatorList) {
            if (!orderValidator.isValid(order)) {
                throw orderValidator.getException();
            }
        }
        return false;
    }
}
