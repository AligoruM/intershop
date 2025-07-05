package com.practice.intershop.mapper;

import com.practice.intershop.dto.SalesOrderDto;
import com.practice.intershop.model.SalesOrder;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = OrderItemMapper.class)
public interface SalesOrderMapper {

    SalesOrderDto toDto(SalesOrder salesOrder);

    List<SalesOrderDto> toDto(List<SalesOrder> salesOrders);
}
