package com.practice.intershop.utils;

import com.practice.intershop.model.ShowcaseItem;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ShowcaseTestDataFactory {
    public static final String NAME_1 = "test showcase item 1";
    public static final String DESCRIPTION_1 = "test showcase item description 1";
    public static final String IMAGE_PATH_1 = "test showcase item image path 1";
    public static final BigDecimal PRICE_1 = BigDecimal.TEN.setScale(2, RoundingMode.HALF_UP);

    public static final String NAME_2 = "test showcase item 2";
    public static final String DESCRIPTION_2 = "test showcase item description 2";
    public static final String IMAGE_PATH_2 = "test showcase item image path 2";
    public static final BigDecimal PRICE_2 = BigDecimal.TWO.setScale(2, RoundingMode.HALF_UP);

    public static ShowcaseItem createShowcaseItem1() {
        ShowcaseItem item = new ShowcaseItem();
        item.setName(NAME_1);
        item.setDescription(DESCRIPTION_1);
        item.setImagePath(IMAGE_PATH_1);
        item.setPrice(PRICE_1);
        return item;
    }

    public static ShowcaseItem createShowcaseItem2() {
        ShowcaseItem item = new ShowcaseItem();
        item.setName(NAME_2);
        item.setDescription(DESCRIPTION_2);
        item.setImagePath(IMAGE_PATH_2);
        item.setPrice(PRICE_2);
        return item;
    }
}