package com.practice.intershop.controller;

import com.practice.intershop.dto.ShowcaseItemDto;
import com.practice.intershop.mapper.ShowcaseItemMapper;
import com.practice.intershop.model.ShowcaseItem;
import com.practice.intershop.repository.ShowcaseItemRepository;
import com.practice.intershop.utils.ShowcaseTestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ShowcaseControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ShowcaseItemMapper showcaseItemMapper;
    @Autowired
    private ShowcaseItemRepository showcaseItemRepository;

    @Test
    void testMainItems_DefaultRequest_shouldReturnPageWithUpTo5Elements() throws Exception {
        ShowcaseItem item1 = ShowcaseTestDataFactory.createShowcaseItem1();
        ShowcaseItem item2 = ShowcaseTestDataFactory.createShowcaseItem2();
        List<ShowcaseItem> savedItems = showcaseItemRepository.saveAll(List.of(item1, item2));

        List<ShowcaseItemDto> expectedDtos = savedItems.stream()
                .map(showcaseItemMapper::toDto)
                .collect(Collectors.toList());


        MvcResult mvcResult = mockMvc.perform(get("/main/items"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andExpect(model().attributeExists("items"))
                .andExpect(model().attribute("sort", "NO"))
                .andExpect(model().attribute("pageNumber", 0))
                .andExpect(model().attribute("pageSize", 5))
                .andReturn();

        Page<ShowcaseItemDto> items = getPageFromModel(mvcResult, "items");
        assertThat(items).hasSize(2);
        assertThat(items.getTotalElements()).isEqualTo(2);
        assertThat(items.getTotalPages()).isEqualTo(1);
        assertThat(items.hasNext()).isFalse();
        assertThat(items.getContent()).containsOnlyOnceElementsOf(expectedDtos);
    }

    @Test
    void testMainItems_1elementPerPagePriceSort_shouldReturnPageWith1ElementSorted() throws Exception {
        ShowcaseItem item1 = ShowcaseTestDataFactory.createShowcaseItem1();
        ShowcaseItem item2 = ShowcaseTestDataFactory.createShowcaseItem2();
        List<ShowcaseItem> savedItems = showcaseItemRepository.saveAll(List.of(item1, item2));
        ShowcaseItemDto expectedExpensiveDto = showcaseItemMapper.toDto(savedItems.get(0));
        ShowcaseItemDto expectedLessExpensiveDto = showcaseItemMapper.toDto(savedItems.get(1));

        MvcResult mvcResult = mockMvc.perform(get("/main/items")
                        .param("pageSize", "1")
                        .param("sort", "PRICE"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andExpect(model().attributeExists("items"))
                .andExpect(model().attribute("sort", "PRICE"))
                .andExpect(model().attribute("pageNumber", 0))
                .andExpect(model().attribute("pageSize", 1))
                .andReturn();

        Page<ShowcaseItemDto> items = getPageFromModel(mvcResult, "items");
        assertThat(items).hasSize(1);
        assertThat(items.getTotalElements()).isEqualTo(2);
        assertThat(items.getTotalPages()).isEqualTo(2);
        assertThat(items.hasNext()).isTrue();
        assertThat(items.getContent()).containsOnlyOnce(expectedLessExpensiveDto);

        mvcResult = mockMvc.perform(get("/main/items")
                        .param("pageSize", "1")
                        .param("pageNumber", "1")
                        .param("sort", "PRICE"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andExpect(model().attributeExists("items"))
                .andExpect(model().attribute("sort", "PRICE"))
                .andExpect(model().attribute("pageNumber", 1))
                .andExpect(model().attribute("pageSize", 1))
                .andReturn();

        items = getPageFromModel(mvcResult, "items");
        assertThat(items).hasSize(1);
        assertThat(items.getTotalElements()).isEqualTo(2);
        assertThat(items.getTotalPages()).isEqualTo(2);
        assertThat(items.hasNext()).isFalse();
        assertThat(items.getContent()).containsOnlyOnce(expectedExpensiveDto);
    }

    @Test
    void testShowcaseItem_mainPath_shouldReturnItemPageWithCorrectItem() throws Exception {
        ShowcaseItem item = ShowcaseTestDataFactory.createShowcaseItem1();
        ShowcaseItem savedItem = showcaseItemRepository.save(item);
        ShowcaseItemDto expectedDto = showcaseItemMapper.toDto(savedItem);

        MvcResult result = mockMvc.perform(get("/main/items/" + savedItem.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("item"))
                .andExpect(model().attributeExists("item"))
                .andReturn();

        ShowcaseItemDto actualDto = getObjectFromModel(result, "item");
        assertThat(actualDto).isEqualTo(expectedDto);
    }

    @Test
    void testMainItems_withSearchParameterFullName_shouldReturnFilteredResults() throws Exception {
        ShowcaseItem item1 = ShowcaseTestDataFactory.createShowcaseItem1();
        ShowcaseItem item2 = ShowcaseTestDataFactory.createShowcaseItem2();
        showcaseItemRepository.saveAll(List.of(item1, item2));

        MvcResult result = mockMvc.perform(get("/main/items")
                        .param("search", "test showcase item 1"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andExpect(model().attributeExists("items"))
                .andReturn();

        Page<ShowcaseItemDto> items = getPageFromModel(result, "items");
        assertThat(items).hasSize(1);
        assertThat(items.getContent().getFirst().getName()).containsIgnoringCase("test showcase item 1");
    }

    @Test
    void testMainItems_withSearchParameterPartDescription_shouldReturnFilteredResults() throws Exception {
        ShowcaseItem item1 = ShowcaseTestDataFactory.createShowcaseItem1();
        ShowcaseItem item2 = ShowcaseTestDataFactory.createShowcaseItem2();
        showcaseItemRepository.saveAll(List.of(item1, item2));

        MvcResult result = mockMvc.perform(get("/main/items")
                        .param("search", "item description 1"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andExpect(model().attributeExists("items"))
                .andReturn();

        Page<ShowcaseItemDto> items = getPageFromModel(result, "items");
        assertThat(items).hasSize(1);
        assertThat(items.getContent().getFirst().getDescription()).containsIgnoringCase("item description 1");
    }

    @Test
    void testShowcaseItem_invalidId_shouldReturn404() throws Exception {
        long nonExistentId = 99999L;

        mockMvc.perform(get("/main/items/" + nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("error", "Showcase item not found"));
    }
}