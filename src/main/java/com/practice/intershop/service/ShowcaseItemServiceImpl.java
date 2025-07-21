package com.practice.intershop.service;

import com.practice.intershop.exception.NotFoundException;
import com.practice.intershop.model.ShowcaseItem;
import com.practice.intershop.repository.ShowcaseItemR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowcaseItemServiceImpl implements ShowcaseService {

    private final ShowcaseItemR2dbcRepository showcaseItemR2dbcRepository;
    private final DatabaseClient databaseClient;

    @Override
    public Mono<Page<ShowcaseItem>> findShowcaseItems(Pageable pageable, String search) {
        Flux<ShowcaseItem> itemsFlux = showcaseItemR2dbcRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search, search, pageable)
                .flatMap(item ->
                        getCountForShowcaseItem(item.getId())
                                .map(count -> {
                                    item.setCount(count);
                                    return item;
                                })
                );

        Mono<Long> countMono = showcaseItemR2dbcRepository.countByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search, search);

        Mono<List<ShowcaseItem>> listMono = itemsFlux.collectList();

        return Mono.zip(listMono, countMono)
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }

    private Mono<Integer> getCountForShowcaseItem(Long showcaseItemId) {
        return databaseClient.sql("SELECT COALESCE(oi.quantity, 0) FROM order_item oi WHERE oi.showcase_item_id = :id and oi.status = 0")
                .bind("id", showcaseItemId)
                .map(row -> row.get(0, Integer.class))
                .one()
                .defaultIfEmpty(0);
    }

    @Override
    public Mono<ShowcaseItem> getShowcaseItem(Long id) {
        return showcaseItemR2dbcRepository.findById(id)
                .flatMap(item ->
                        getCountForShowcaseItem(item.getId())
                                .map(count -> {
                                    item.setCount(count);
                                    return item;
                                })
                )
                .switchIfEmpty(Mono.error(new NotFoundException("Showcase item not found")));
    }
}
