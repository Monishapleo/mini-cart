package com.minicart.orderservice.kafka;

import com.minicart.orderservice.entity.Order;
import com.minicart.orderservice.enums.OrderStatus;
import com.minicart.orderservice.enums.PaymentStatus;
import com.minicart.orderservice.event.PaymentEvent;
import com.minicart.orderservice.repository.OrderRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PaymentEventConsumer {

    private final OrderRepository repository;

    public PaymentEventConsumer(OrderRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "payment.completed", groupId = "order-group")
    public void consumePayment(PaymentEvent event) {

        System.out.println("Received payment event for order: " + event.getOrderId());

        Order order = repository.findById(event.getOrderId())
                .orElse(null);

        if (order == null) {
            System.out.println("Order not found for id: " + event.getOrderId());
            return;
        }

        if (OrderStatus.CONFIRMED.name().equals(order.getStatus()) ||
                OrderStatus.PAYMENT_FAILED.name().equals(order.getStatus())) {

            System.out.println("Order already processed: " + order.getId());
            return;
        }

        if (PaymentStatus.SUCCESS.name().equals(event.getStatus())) {
            order.setStatus(OrderStatus.CONFIRMED.name());
        } else {
            order.setStatus(OrderStatus.PAYMENT_FAILED.name());
        }

        repository.save(order);

        System.out.println("Order updated: " + order.getId());
    }
}