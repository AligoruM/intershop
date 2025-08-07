package com.practice.intershop.utils;

import com.practice.intershop.dto.ShowcaseItemDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HtmlTestUtils {

    public static List<ShowcaseItemDto> extractItemsFromHtml(EntityExchangeResult<byte[]> response) {
        String html = new String(response.getResponseBody(), StandardCharsets.UTF_8);
        Document doc = Jsoup.parse(html);

        Elements itemCards = doc.select("td.item-card");
        List<ShowcaseItemDto> items = new ArrayList<>();

        for (Element card : itemCards) {
            Elements bolds = card.select("b");
            String name = bolds.get(0).text();
            String priceText = bolds.get(1).text().replace(" руб.", "").trim();
            double price = Double.parseDouble(priceText);

            String description = card.select("p").text();

            ShowcaseItemDto dto = new ShowcaseItemDto();
            dto.setName(name);
            dto.setPrice(BigDecimal.valueOf(price));
            dto.setDescription(description);

            items.add(dto);
        }

        return items;
    }

    public static ShowcaseItemDto extractItemFromHtml(EntityExchangeResult<byte[]> response) {
        Document document = Jsoup.parse(new String(response.getResponseBody(), StandardCharsets.UTF_8));

        Element wrapper = document.selectFirst(".item-wrapper");
        if (wrapper == null) {
            throw new IllegalStateException("Item wrapper not found in HTML");
        }

        String name = wrapper.select("b").get(0).text();
        String priceText = wrapper.select("b").get(1).text();
        double price = Double.parseDouble(priceText.replace(" руб.", "").trim());

        String description = wrapper.select("span").first().text();

        ShowcaseItemDto dto = new ShowcaseItemDto();
        dto.setName(name);
        dto.setPrice(BigDecimal.valueOf(price));
        dto.setDescription(description);

        return dto;
    }
}
