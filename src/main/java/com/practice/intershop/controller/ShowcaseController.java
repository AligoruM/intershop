package com.practice.intershop.controller;

import com.practice.intershop.enums.SortOption;
import com.practice.intershop.mapper.ShowcaseItemMapper;
import com.practice.intershop.service.ShowcaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ShowcaseController {

    private final ShowcaseService showcaseService;
    private final ShowcaseItemMapper showcaseItemMapper;

    @GetMapping("/main/items")
    public Mono<Rendering> mainItems(@RequestParam(value = "search") Optional<String> search,
                                     @RequestParam(value = "sort", defaultValue = "NO") SortOption sort,
                                     @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
                                     @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {

        Pageable pageable = getPageable(pageNumber, pageSize, sort);
        String searchValue = search.orElse("");

        return showcaseService.findShowcaseItems(pageable, searchValue)
                .map(page -> Rendering.view("main")
                        .modelAttribute("items", page.map(showcaseItemMapper::toDto))
                        .modelAttribute("search", searchValue)
                        .modelAttribute("sort", sort.toString())
                        .modelAttribute("pageNumber", pageNumber)
                        .modelAttribute("pageSize", pageSize)
                        .build());
    }

    @GetMapping({"/items/{id}", "/main/items/{id}"})
    public Mono<Rendering> showcaseItem(@PathVariable Long id) {
        return showcaseService.getShowcaseItem(id)
                .map(item -> Rendering.view("item")
                        .modelAttribute("item", showcaseItemMapper.toDto(item)).build());
    }

    private Pageable getPageable(int page, int size, SortOption sortOption) {
        return switch (sortOption) {
            case ALPHA -> PageRequest.of(page, size, Sort.by("name").ascending());
            case PRICE -> PageRequest.of(page, size, Sort.by("price").ascending());
            default -> PageRequest.of(page, size); // без сортировки
        };
    }
}
