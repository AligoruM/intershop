package com.practice.intershop.controller;

import com.practice.intershop.dto.ShowcaseItemDto;
import com.practice.intershop.dto.SortOption;
import com.practice.intershop.mapper.ShowcaseItemMapper;
import com.practice.intershop.model.ShowcaseItem;
import com.practice.intershop.service.ShowcaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ShowcaseController {

    private final ShowcaseService showcaseService;
    private final ShowcaseItemMapper showcaseItemMapper;

    @GetMapping("/main/items")
    public String mainItems(@RequestParam(value = "search") Optional<String> search,
                            @RequestParam(value = "sort", defaultValue = "NO") SortOption sort,
                            @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
                            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                            Model model) {

        Pageable pageable = getPageable(pageNumber, pageSize, sort);

        Page<ShowcaseItemDto> showcaseItemPage = showcaseService
                .findShowcaseItems(pageable, search.orElse(""))
                .map(showcaseItemMapper::toShowcaseItemDto);

        search.ifPresent(searchValue -> model.addAttribute("search", searchValue));
        model.addAttribute("sort", sort.toString());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("items", showcaseItemPage);
        return "main";
    }

    @GetMapping("/items/{id}")
    public String showcaseItem(@PathVariable Long id, Model model) {
        ShowcaseItem showcaseItem = showcaseService.findShowcaseItem(id);
        model.addAttribute("item", showcaseItemMapper.toShowcaseItemDto(showcaseItem));
        return "item";
    }

    private Pageable getPageable(int page, int size, SortOption sortOption) {
        return switch (sortOption) {
            case ALPHA -> PageRequest.of(page, size, Sort.by("name").ascending());
            case PRICE -> PageRequest.of(page, size, Sort.by("price").ascending());
            default -> PageRequest.of(page, size); // без сортировки
        };
    }
}
