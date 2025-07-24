package com.practice.intershop.model.converters.orderItem;

import com.practice.intershop.enums.OrderItemStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class ByteToOrderItemStatusConverter implements Converter<Byte, OrderItemStatus> {
    @Override
    public OrderItemStatus convert(Byte source) {
        return OrderItemStatus.values()[source];
    }
}
