package com.practice.intershop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@SuppressWarnings("unchecked")
public abstract class AbstractControllerTest {

    @Autowired
    protected WebTestClient webTestClient;

    protected <T> Page<T> getPageFromModel(MvcResult result, String attributeName) {
        return null;
    }

    protected <T> List<T> getListFromModel(MvcResult result, String attributeName) {
        return null;
    }

    protected <T> T getObjectFromModel(MvcResult result, String attributeName) {
        return null;
    }
}
