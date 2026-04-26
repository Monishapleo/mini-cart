package com.minicart.orderservice.event;

import com.minicart.orderservice.entity.Order;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {

    private String eventId;
    private String eventType;
    private LocalDateTime timestamp;
    private Order payload;
}