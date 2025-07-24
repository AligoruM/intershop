package com.practice.intershop.repository;

import com.practice.intershop.model.ShowcaseItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ShowcaseItemR2dbcRepository extends R2dbcRepository<ShowcaseItem, Long> {

    Flux<ShowcaseItem> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);

    Flux<ShowcaseItem> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description, Pageable pageable);

    Mono<Long> countByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);

}
