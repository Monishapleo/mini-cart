package com.minicart.orderservice.kafka;

import com.minicart.orderservice.event.OrderEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderCreatedEvent(OrderEvent event, Long orderId) {

        kafkaTemplate.send(
                "order.created",
                String.valueOf(orderId), // KEY
                event
        );

        System.out.println("Kafka Event Sent: " + event.getEventId());
    }

}