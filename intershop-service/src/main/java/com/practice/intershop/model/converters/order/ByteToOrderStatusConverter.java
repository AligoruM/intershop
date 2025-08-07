package com.practice.intershop.model.converters.order;

import com.practice.intershop.enums.OrderStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class ByteToOrderStatusConverter implements Converter<Byte, OrderStatus> {
    @Override
    public OrderStatus convert(Byte source) {
        return OrderStatus.values()[source];
    }
}
