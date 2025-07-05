package com.practice.intershop.repository;

import com.practice.intershop.enums.OrderStatus;
import com.practice.intershop.model.SalesOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends CrudRepository<SalesOrder, Long> {
    Optional<SalesOrder> findFirstByOrderStatus(OrderStatus orderStatus);

    List<SalesOrder> findAllByOrderStatus(OrderStatus orderStatus);
}
