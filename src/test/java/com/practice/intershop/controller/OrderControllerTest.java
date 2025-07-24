package com.practice.intershop.controller;

/*import com.practice.intershop.dto.SalesOrderDto;
import com.practice.intershop.mapper.SalesOrderMapper;
import com.practice.intershop.model.OrderItem;
import com.practice.intershop.model.SalesOrder;
import com.practice.intershop.repository.OrderR2dbcRepository;
import com.practice.intershop.repository.OrderRepository;
import com.practice.intershop.repository.ShowcaseItemR2dbcRepository;
import com.practice.intershop.repository.ShowcaseItemRepository;
import com.practice.intershop.utils.OrderTestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class OrderControllerTest extends AbstractControllerTest {

    @Autowired
    private SalesOrderMapper salesOrderMapper;
    @Autowired
    private OrderR2dbcRepository salesOrderRepository;
    @Autowired
    private ShowcaseItemR2dbcRepository salesShowcaseItemRepository;

    @Test
    void testGetOrderById_shouldReturnOrderPage() throws Exception {
        SalesOrder order = OrderTestDataFactory.createCompletedOrder1();
        persistShowcaseItems(order);
        SalesOrder saved = salesOrderRepository.save(order);
        SalesOrderDto expectedDto = salesOrderMapper.toDto(saved);

        MvcResult result = mockMvc.perform(get("/orders/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("order"))
                .andExpect(model().attributeExists("salesOrder"))
                .andReturn();

        SalesOrderDto actualDto = getObjectFromModel(result, "salesOrder");

        assertThat(actualDto).isEqualTo(expectedDto);
    }

    @Test
    void testGetAllCompletedOrders_shouldReturnOrdersPage() throws Exception {
        SalesOrder order1 = OrderTestDataFactory.createCompletedOrder1();
        SalesOrder order2 = OrderTestDataFactory.createCompletedOrder2();
        persistShowcaseItems(order1, order2);
        List<SalesOrder> savedOrders = salesOrderRepository.saveAll(List.of(order1, order2));

        List<SalesOrderDto> expectedDtos = savedOrders.stream()
                .map(salesOrderMapper::toDto)
                .toList();

        MvcResult mvcResult = mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(model().attributeExists("orders"))
                .andReturn();
        List<SalesOrderDto> actualDtos = getListFromModel(mvcResult, "orders");

        assertThat(actualDtos)
                .hasSameSizeAs(expectedDtos)
                .containsExactlyInAnyOrderElementsOf(expectedDtos);
    }

    //here should be more tests, but I have no time to write them :(

    private void persistShowcaseItems(SalesOrder... salesOrders) {
        Arrays.stream(salesOrders)
                .flatMap(so -> so.getOrderItems().stream())
                .map(OrderItem::getShowcaseItem)
                .distinct()
                .forEach(showcaseItem -> salesShowcaseItemRepository.save(showcaseItem));
    }
}*/
