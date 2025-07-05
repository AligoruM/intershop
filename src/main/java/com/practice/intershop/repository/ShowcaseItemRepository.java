package com.practice.intershop.repository;

import com.practice.intershop.model.ShowcaseItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowcaseItemRepository extends JpaRepository<ShowcaseItem, Long> {
    List<ShowcaseItem> findShowcaseItemByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);

    Page<ShowcaseItem> findShowcaseItemByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description, Pageable pageable);
}
