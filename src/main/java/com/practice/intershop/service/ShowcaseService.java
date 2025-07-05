package com.practice.intershop.service;

import com.practice.intershop.model.ShowcaseItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShowcaseService {
    Page<ShowcaseItem> findShowcaseItems(Pageable pageable, String search);

    ShowcaseItem getShowcaseItem(Long id);
}
