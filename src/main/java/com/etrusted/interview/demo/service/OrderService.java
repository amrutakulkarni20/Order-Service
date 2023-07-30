package com.etrusted.interview.demo.service;

import com.etrusted.interview.demo.model.OrderModel;
import com.etrusted.interview.demo.model.OrderResponseModel;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface OrderService {

    OrderResponseModel createOrder(OrderModel order);

    OrderModel getOrderByOrderId(long orderId);

    List<OrderModel> getOrdersByShopId(long shopId, Pageable pageable);
}
