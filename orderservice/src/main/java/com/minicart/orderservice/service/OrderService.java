package com.minicart.orderservice.service;

import com.minicart.orderservice.entity.Order;
import com.minicart.orderservice.event.OrderEvent;
import com.minicart.orderservice.kafka.OrderEventProducer;
import com.minicart.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderEventProducer orderEventProducer;
    public Order createOrder(Order order) {

        order.setStatus("PENDING");
        Order saved = orderRepository.save(order);

        OrderEvent event = new OrderEvent(
                UUID.randomUUID().toString(),
                "ORDER_CREATED",
                LocalDateTime.now(),
                saved
        );

        orderEventProducer.sendOrderCreatedEvent(event, saved.getId());

        return saved;
    }
}