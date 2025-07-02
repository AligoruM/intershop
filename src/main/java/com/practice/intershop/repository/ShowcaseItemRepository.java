package com.practice.intershop.repository;

import com.practice.intershop.model.ShowcaseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowcaseItemRepository extends JpaRepository<ShowcaseItem, Long> {
}
