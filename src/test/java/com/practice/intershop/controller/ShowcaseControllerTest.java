package com.practice.intershop.controller;

import com.practice.intershop.dto.ShowcaseItemDto;
import com.practice.intershop.mapper.ShowcaseItemMapper;
import com.practice.intershop.model.ShowcaseItem;
import com.practice.intershop.repository.ShowcaseItemR2dbcRepository;
import com.practice.intershop.utils.HtmlTestUtils;
import com.practice.intershop.utils.ShowcaseTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ShowcaseControllerTest extends AbstractControllerTest {

    @Autowired
    private ShowcaseItemMapper showcaseItemMapper;
    @Autowired
    private ShowcaseItemR2dbcRepository showcaseItemRepository;

    @BeforeEach
    void setup() {
        showcaseItemRepository.deleteAll().block();
    }

    @Test
    void testMainItems_DefaultRequest_shouldReturnPageWithUpTo5Elements() throws Exception {
        ShowcaseItem item1 = ShowcaseTestDataFactory.createShowcaseItem1();
        ShowcaseItem item2 = ShowcaseTestDataFactory.createShowcaseItem2();

        List<ShowcaseItem> savedItems = Flux.just(item1, item2)
                .flatMap(showcaseItemRepository::save)
                .collectList()
                .block();

        List<ShowcaseItemDto> expectedDtos = savedItems.stream()
                .map(showcaseItemMapper::toDto)
                .toList();

        webTestClient.get().uri("/main/items")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    List<ShowcaseItemDto> actualItems = HtmlTestUtils.extractItemsFromHtml(response);

                    assertThat(actualItems).hasSize(2);

                    for (ShowcaseItemDto expected : expectedDtos) {
                        boolean matchFound = actualItems.stream().anyMatch(actual ->
                                expected.getName().equals(actual.getName()) &&
                                        expected.getPrice().compareTo(actual.getPrice()) == 0
                        );
                        assertThat(matchFound)
                                .as("Товар с именем '%s' и ценой %.2f найден", expected.getName(), expected.getPrice())
                                .isTrue();
                    }
                });
    }

    @Test
    void testMainItems_1elementPerPagePriceSort_shouldReturnPageWith1ElementSorted() {
        ShowcaseItem item1 = ShowcaseTestDataFactory.createShowcaseItem1(); // дороже
        ShowcaseItem item2 = ShowcaseTestDataFactory.createShowcaseItem2(); // дешевле

        List<ShowcaseItem> savedItems = Flux.just(item1, item2)
                .flatMap(showcaseItemRepository::save)
                .collectList()
                .block();

        List<ShowcaseItemDto> dtos = savedItems.stream()
                .map(showcaseItemMapper::toDto)
                .sorted(Comparator.comparing(ShowcaseItemDto::getPrice)) // сортировка по цене
                .toList();

        ShowcaseItemDto cheaper = dtos.get(0);
        ShowcaseItemDto moreExpensive = dtos.get(1);

        // Первая страница
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/main/items")
                        .queryParam("pageSize", "1")
                        .queryParam("sort", "PRICE")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    List<ShowcaseItemDto> items = HtmlTestUtils.extractItemsFromHtml(response);
                    assertThat(items).hasSize(1);
                    assertThat(items.get(0).getName()).isEqualTo(cheaper.getName());
                    assertThat(items.get(0).getPrice()).isEqualByComparingTo(cheaper.getPrice());
                });

        // Вторая страница
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/main/items")
                        .queryParam("pageSize", "1")
                        .queryParam("pageNumber", "1")
                        .queryParam("sort", "PRICE")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    List<ShowcaseItemDto> items = HtmlTestUtils.extractItemsFromHtml(response);
                    assertThat(items).hasSize(1);
                    assertThat(items.get(0).getName()).isEqualTo(moreExpensive.getName());
                    assertThat(items.get(0).getPrice()).isEqualByComparingTo(moreExpensive.getPrice());
                });
    }

    @Test
    void testShowcaseItem_mainPath_shouldReturnItemPageWithCorrectItem() {
        ShowcaseItem item = ShowcaseTestDataFactory.createShowcaseItem1();
        ShowcaseItem savedItem = showcaseItemRepository.save(item).block();
        ShowcaseItemDto expected = showcaseItemMapper.toDto(savedItem);

        webTestClient.get().uri("/main/items/" + savedItem.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    ShowcaseItemDto actual = HtmlTestUtils.extractItemFromHtml(response);
                    assertThat(actual.getName()).isEqualTo(expected.getName());
                    assertThat(actual.getPrice()).isEqualByComparingTo(expected.getPrice());
                    assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
                });
    }

    @Test
    void testMainItems_withSearchParameterFullName_shouldReturnFilteredResults() {
        ShowcaseItem item1 = ShowcaseTestDataFactory.createShowcaseItem1();
        ShowcaseItem item2 = ShowcaseTestDataFactory.createShowcaseItem2();

        Flux.just(item1, item2)
                .flatMap(showcaseItemRepository::save)
                .then()
                .block();

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/main/items")
                        .queryParam("search", "test showcase item 1")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    List<ShowcaseItemDto> items = HtmlTestUtils.extractItemsFromHtml(response);
                    assertThat(items).hasSize(1);
                    assertThat(items.get(0).getName()).containsIgnoringCase("test showcase item 1");
                });
    }

    @Test
    void testMainItems_withSearchParameterPartDescription_shouldReturnFilteredResults() {
        ShowcaseItem item1 = ShowcaseTestDataFactory.createShowcaseItem1();
        ShowcaseItem item2 = ShowcaseTestDataFactory.createShowcaseItem2();

        Flux.just(item1, item2)
                .flatMap(showcaseItemRepository::save)
                .then()
                .block();

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/main/items")
                        .queryParam("search", "item description 1")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    List<ShowcaseItemDto> items = HtmlTestUtils.extractItemsFromHtml(response);
                    assertThat(items).hasSize(1);
                    assertThat(items.get(0).getDescription()).containsIgnoringCase("item description 1");
                });
    }

    @Test
    void testShowcaseItem_invalidId_shouldReturn404() {
        long nonExistentId = 99999L;

        webTestClient.get().uri("/main/items/" + nonExistentId)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String html = response.getResponseBody();
                    assertThat(html).contains("Showcase item not found");
                });
    }
}
