package com.etrusted.interview.demo.controller;

import com.etrusted.interview.demo.model.OrderModel;
import com.etrusted.interview.demo.model.OrderResponseModel;
import com.etrusted.interview.demo.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class OrderController implements IOrderController{

    private OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping("/order")
    public OrderResponseModel createOrder(@Valid @RequestBody OrderModel order){
        return orderService.createOrder(order);
    }

    @GetMapping("/order/{orderId}")
    public OrderModel getOrderByOrderId(@PathVariable("orderId") long orderId){
        return orderService.getOrderByOrderId(orderId);
    }

    @GetMapping("/orders/{shopId}")
    public List<OrderModel> getOrdersByShopId(@PathVariable("shopId") long shopId, Pageable pageable){
        return orderService.getOrdersByShopId(shopId, pageable);
    }
}
