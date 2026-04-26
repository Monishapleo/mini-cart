package com.minicart.paymentservice.service;

import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    @Value("${razorpay.key}")
    private String key;

    @Value("${razorpay.secret}")
    private String secret;

    public boolean processPayment(double amount) {

        try {
            RazorpayClient client = new RazorpayClient(key, secret);

            JSONObject options = new JSONObject();
            options.put("amount", (int)(amount * 100)); // paise
            options.put("currency", "INR");
            options.put("receipt", "order_rcptid_11");

            client.orders.create(options);

            return true; // created successfully

        } catch (Exception e) {
            return false;
        }
    }
}
