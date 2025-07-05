package com.practice.intershop.service;

import com.practice.intershop.model.ShowcaseItem;
import com.practice.intershop.repository.ShowcaseItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShowcaseItemServiceImpl implements ShowcaseService {

    private final ShowcaseItemRepository showcaseItemRepository;

    @Override
    public Page<ShowcaseItem> findShowcaseItems(Pageable pageable, String search) {
        return showcaseItemRepository
                .findShowcaseItemByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search, search, pageable);
    }

    @Override
    public ShowcaseItem getShowcaseItem(Long id) {
        return showcaseItemRepository.getReferenceById(id);
    }
}
