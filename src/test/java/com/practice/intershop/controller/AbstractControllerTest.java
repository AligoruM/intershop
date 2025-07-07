package com.practice.intershop.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@SuppressWarnings("unchecked")
public abstract class AbstractControllerTest {

    protected <T> Page<T> getPageFromModel(MvcResult result, String attributeName) {
        return (Page<T>) result.getModelAndView().getModel().get(attributeName);
    }

    protected <T> List<T> getListFromModel(MvcResult result, String attributeName) {
        return (List<T>) result.getModelAndView().getModel().get(attributeName);
    }

    protected <T> T getObjectFromModel(MvcResult result, String attributeName) {
        return (T) result.getModelAndView().getModel().get(attributeName);
    }
}
