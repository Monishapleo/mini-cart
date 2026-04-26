package com.minicart.paymentservice.service;

import com.minicart.paymentservice.entity.Payment;
import com.minicart.paymentservice.event.OrderEvent;
import com.minicart.paymentservice.event.PaymentEvent;
import com.minicart.paymentservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentConsumer {

    private final PaymentRepository repository;;
    private final PaymentEventProducer paymentEventProducer;
    private final RazorpayService razorpayService;

    /*public PaymentConsumer(PaymentRepository repository) {
        this.repository = repository;
    }*/
    public PaymentConsumer(PaymentRepository repository,
                           PaymentEventProducer producer,
                           RazorpayService razorpayService) {
        this.repository = repository;
        this.paymentEventProducer = producer;
        this.razorpayService = razorpayService;
    }
    @KafkaListener(topics = "order.created", groupId = "payment-group")
    public void consume(OrderEvent event) {

        Long orderId = event.getPayload().getId();

        // Idempotency check
        if (repository.existsById(orderId)) {
            System.out.println("Payment already processed for order: " + orderId);
            return;
        }

        boolean isSuccess;

        try {
            isSuccess = razorpayService.processPayment(
                    event.getPayload().getPrice()
            );
        } catch (Exception e) {
            isSuccess = false;
        }

        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(event.getPayload().getPrice());
        payment.setStatus(isSuccess ? "SUCCESS" : "FAILED");

        repository.save(payment);

        System.out.println("Payment " + payment.getStatus() +
                " for order: " + orderId);

        PaymentEvent paymentEvent = new PaymentEvent(
                UUID.randomUUID().toString(),
                isSuccess ? "PAYMENT_SUCCESS" : "PAYMENT_FAILED",
                orderId,
                payment.getStatus()
        );

        paymentEventProducer.sendPaymentEvent(paymentEvent);
    }
}
