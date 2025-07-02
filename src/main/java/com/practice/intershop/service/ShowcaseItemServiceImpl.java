package com.practice.intershop.service;

import com.practice.intershop.model.ShowcaseItem;
import com.practice.intershop.repository.ShowcaseItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowcaseItemServiceImpl implements ShowcaseService {

    private ShowcaseItemRepository showcaseItemRepository;

    @Override
    public List<ShowcaseItem> findShowcaseItems(PageRequest pageRequest, String search, String sort) {
        ShowcaseItem showcaseItem = new ShowcaseItem();
        showcaseItem.setId(1L);
        showcaseItem.setName("Showcase Item");
        showcaseItem.setDescription("Showcase Item");
        showcaseItem.setPrice(new BigDecimal("1000"));
        return List.of(showcaseItem);
    }
}
