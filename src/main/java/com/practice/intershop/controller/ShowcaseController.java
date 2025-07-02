package com.practice.intershop.controller;

import com.practice.intershop.dto.ShowcaseItemDto;
import com.practice.intershop.mapper.ShowcaseItemMapper;
import com.practice.intershop.model.ShowcaseItem;
import com.practice.intershop.service.ShowcaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ShowcaseController {

    private final ShowcaseService showcaseService;
    private final ShowcaseItemMapper showcaseItemMapper;

    @GetMapping("/main/items")
    public String mainItems(@RequestParam(value = "search") Optional<String> search,
                            @RequestParam(value = "sort", defaultValue = "NO") String sort,
                            @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
                            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                            Model model) {

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        List<ShowcaseItem> showcaseItems = showcaseService.findShowcaseItems(pageRequest, search.orElse(null), sort);
        Page<ShowcaseItemDto> showcaseItemDtoPage = new PageImpl<>(
                showcaseItemMapper.toShowcaseItemDtos(showcaseItems), pageRequest, 1
        );

        search.ifPresent(searchValue -> model.addAttribute("search", searchValue));
        model.addAttribute("sort", sort);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("items", showcaseItemDtoPage);
        return "main";
    }
}
