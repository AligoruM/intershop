package com.practice.intershop.service.validation;

import com.practice.intershop.model.SalesOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderValidationServiceImpl implements OrderValidationService {

    private final List<OrderValidator> orderValidatorList;

    @Override
    public Mono<Void> isValid(SalesOrder order) {
        return Flux.fromIterable(orderValidatorList)
                .concatMap(validator -> validator.validate(order))
                .then();
    }
}
