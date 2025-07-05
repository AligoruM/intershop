package com.practice.intershop.mapper;

import com.practice.intershop.dto.OrderItemDto;
import com.practice.intershop.model.OrderItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ShowcaseItemMapper.class})
public interface OrderItemMapper {

    OrderItemDto toDto(OrderItem orderItem);

    List<OrderItemDto> toDto(List<OrderItem> orderItems);

}
