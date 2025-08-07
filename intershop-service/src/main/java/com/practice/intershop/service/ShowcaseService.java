package com.practice.intershop.service;

import com.practice.intershop.model.ShowcaseItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

public interface ShowcaseService {
    Mono<Page<ShowcaseItem>> findShowcaseItems(Pageable pageable, String search);

    Mono<ShowcaseItem> getShowcaseItem(Long id);
}
