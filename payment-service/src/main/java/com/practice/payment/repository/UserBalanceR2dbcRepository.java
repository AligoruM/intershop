package com.practice.payment.repository;

import com.practice.payment.model.UserBalance;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserBalanceR2dbcRepository extends ReactiveCrudRepository<UserBalance, Long> {
    Mono<UserBalance> findUserBalanceByUserId(Long userId);
}
