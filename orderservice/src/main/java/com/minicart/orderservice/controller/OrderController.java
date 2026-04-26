package com.minicart.orderservice.controller;

import com.minicart.orderservice.entity.Order;
import com.minicart.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/create-order")
    public Order create(@RequestBody Order order) {
        return orderService.createOrder(order);
    }
}