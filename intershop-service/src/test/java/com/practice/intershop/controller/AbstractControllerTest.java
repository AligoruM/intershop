package com.practice.intershop.controller;

import com.practice.intershop.repository.OrderItemR2dbcRepository;
import com.practice.intershop.repository.OrderR2dbcRepository;
import com.practice.intershop.repository.ShowcaseItemR2dbcRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public abstract class AbstractControllerTest {

    @Autowired
    protected WebTestClient webTestClient;
    @Autowired
    protected OrderR2dbcRepository salesOrderRepository;
    @Autowired
    protected ShowcaseItemR2dbcRepository showcaseItemRepository;
    @Autowired
    protected OrderItemR2dbcRepository orderItemRepository;

    @BeforeEach
    void setUp() {
        orderItemRepository.deleteAll().block();
        salesOrderRepository.deleteAll().block();
        showcaseItemRepository.deleteAll().block();
    }
}
