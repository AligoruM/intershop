package com.practice.intershop.model.converters.order;

import com.practice.intershop.enums.OrderStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class OrderStatusToByteConverter implements Converter<OrderStatus, Byte> {
    @Override
    public Byte convert(OrderStatus source) {
        return (byte) source.ordinal();
    }
}
