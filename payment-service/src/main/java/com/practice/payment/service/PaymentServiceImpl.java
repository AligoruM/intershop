package com.practice.payment.service;

import com.practice.payment.model.UserBalance;
import com.practice.payment.repository.UserBalanceR2dbcRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final UserBalanceR2dbcRepository userBalanceRepository;

    @Override
    public Mono<Boolean> performPayment(Long userId, BigDecimal amount) {
        return getUserBalance(userId)
                .flatMap(balance -> {
                    BigDecimal newBalance = balance.getBalance().subtract(amount);

                    if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                        return Mono.error(new IllegalStateException("Not enough money"));
                    }

                    balance.setBalance(newBalance);
                    return userBalanceRepository.save(balance)
                            .thenReturn(true);
                });
    }

    @Override
    public Mono<BigDecimal> getBalance(Long userId) {
        return getUserBalance(userId)
                .map(UserBalance::getBalance);
    }

    private Mono<UserBalance> getUserBalance(Long userId) {
        return userBalanceRepository.findUserBalanceByUserId(userId)
                .switchIfEmpty(Mono.error(new NotFoundException("User not found")));
    }
}
