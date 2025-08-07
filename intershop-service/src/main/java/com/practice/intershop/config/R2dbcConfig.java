package com.practice.intershop.config;

import com.practice.intershop.model.converters.order.ByteToOrderStatusConverter;
import com.practice.intershop.model.converters.order.OrderStatusToByteConverter;
import com.practice.intershop.model.converters.orderItem.ByteToOrderItemStatusConverter;
import com.practice.intershop.model.converters.orderItem.OrderItemStatusToByteConverter;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class R2dbcConfig extends AbstractR2dbcConfiguration {

    private final ConnectionFactory connectionFactory;

    @Override
    public ConnectionFactory connectionFactory() {
        return connectionFactory;
    }

    @Override
    protected List<Object> getCustomConverters() {
        return List.of(
                new OrderStatusToByteConverter(),
                new ByteToOrderStatusConverter(),
                new OrderItemStatusToByteConverter(),
                new ByteToOrderItemStatusConverter()
        );
    }
}
