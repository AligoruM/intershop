package com.practice.intershop.service;

import com.practice.intershop.exception.NotFoundException;
import com.practice.intershop.model.ShowcaseItem;
import com.practice.intershop.repository.ShowcaseItemR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
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
    private final ReactiveRedisTemplate<String, List<ShowcaseItem>> itemsReactiveRedisTemplate;
    private final ReactiveRedisTemplate<String, Long> countReactiveRedisTemplate;

    @Override
    public Mono<Page<ShowcaseItem>> findShowcaseItems(Pageable pageable, String search) {
        String cacheKey = String.format("showcaseItems:search=%s:sort=%s:page=%d:size=%d",
                search, pageable.getSort(), pageable.getPageNumber(), pageable.getPageSize());

        Mono<Long> countMono = countReactiveRedisTemplate.opsForValue()
                .get(search)
                .switchIfEmpty(
                        showcaseItemR2dbcRepository.countByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search, search)
                                .flatMap(count -> countReactiveRedisTemplate.opsForValue()
                                        .set("itemsCount:search=" + search, count)
                                        .thenReturn(count))
                );

        Mono<List<ShowcaseItem>> rawItemsMono = itemsReactiveRedisTemplate.opsForValue()
                .get(cacheKey)
                .switchIfEmpty(
                        showcaseItemR2dbcRepository
                                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search, search, pageable)
                                .collectList()
                                .flatMap(items -> itemsReactiveRedisTemplate.opsForValue()
                                        .set(cacheKey, items)
                                        .thenReturn(items)
                                )
                );

        Mono<List<ShowcaseItem>> enrichedItemsMono = rawItemsMono
                .flatMapMany(Flux::fromIterable)
                .flatMap(item -> getCountForShowcaseItem(item.getId())
                        .map(count -> {
                            item.setCount(count);
                            return item;
                        })
                )
                .collectList();

        return Mono.zip(enrichedItemsMono, countMono)
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
    @Cacheable(value = "showCaseItem", key = "#p0")
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
