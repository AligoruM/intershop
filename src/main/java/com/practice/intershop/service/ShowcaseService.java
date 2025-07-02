package com.practice.intershop.service;

import com.practice.intershop.model.ShowcaseItem;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ShowcaseService {
    List<ShowcaseItem> findShowcaseItems(PageRequest pageRequest, String search, String sort);
}
