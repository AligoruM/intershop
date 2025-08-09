package com.practice.intershop.controller;

import com.practice.intershop.repository.OrderItemR2dbcRepository;
import com.practice.intershop.repository.OrderR2dbcRepository;
import com.practice.intershop.repository.ShowcaseItemR2dbcRepository;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@Testcontainers
public abstract class AbstractControllerTest {

    @Autowired
    protected WebTestClient webTestClient;
    @Autowired
    protected OrderR2dbcRepository salesOrderRepository;
    @Autowired
    protected ShowcaseItemR2dbcRepository showcaseItemRepository;
    @Autowired
    protected OrderItemR2dbcRepository orderItemRepository;

    private static final RedisContainer redisContainer = new RedisContainer(DockerImageName.parse("redis:7.0-alpine"));

    @BeforeAll
    static void setup() {
        redisContainer.start();
    }

    @DynamicPropertySource
    static void registerRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redisContainer::getRedisHost);
        registry.add("spring.data.redis.port", redisContainer::getRedisPort);
    }

    @BeforeEach
    void setUp() {
        orderItemRepository.deleteAll().block();
        salesOrderRepository.deleteAll().block();
        showcaseItemRepository.deleteAll().block();
    }
}
