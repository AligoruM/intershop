package com.practice.intershop.model.converters.orderItem;

import com.practice.intershop.enums.OrderItemStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class OrderItemStatusToByteConverter implements Converter<OrderItemStatus, Byte> {
    @Override
    public Byte convert(OrderItemStatus source) {
        return (byte) source.ordinal();
    }
}
