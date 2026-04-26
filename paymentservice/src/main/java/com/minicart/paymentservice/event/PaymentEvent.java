package com.minicart.paymentservice.event;

import lombok.Data;

@Data
public class PaymentEvent {

    private String eventId;
    private String eventType;
    private Long orderId;
    private String status; // SUCCESS / FAILED

    public PaymentEvent(String eventId, String eventType, Long orderId, String status) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.orderId = orderId;
        this.status = status;
    }
}
