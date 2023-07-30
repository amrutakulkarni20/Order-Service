package com.etrusted.interview.demo.service;

import com.etrusted.interview.demo.entity.Order;
import com.etrusted.interview.demo.entity.Shop;
import com.etrusted.interview.demo.entity.User;
import com.etrusted.interview.demo.model.OrderModel;
import com.etrusted.interview.demo.model.OrderResponseModel;
import com.etrusted.interview.demo.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    private ShopService shopService;
    private UserService userService;

    private ModelMapper modelMapper;


    public OrderServiceImpl(ShopService shopService, UserService userService, OrderRepository orderRepository, ModelMapper modelMapper) {
        this.shopService = shopService;
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public OrderResponseModel createOrder(OrderModel orderModel) {

        final Optional<User> user = userService.findUserByEmail(orderModel.getUser().getEmail());
        final Optional<Shop> shop = shopService.findShopByUrl(orderModel.getShop().getUrl());

        final Order orderToCreate = Order.builder()
                .user(user.orElse(modelMapper.map(orderModel.getUser(), User.class)))
                .shop(shop.orElse(modelMapper.map(orderModel.getShop(), Shop.class)))
                .orderReference(orderModel.getOrderReference())
                .paymentType(orderModel.getPaymentType())
                .build();

        orderRepository.save(orderToCreate);

        return OrderResponseModel.builder()
                .orderId(orderToCreate.getId())
                .build();
    }

    @Override
    public OrderModel getOrderByOrderId(long orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow();
        return modelMapper.map(order, OrderModel.class);
    }

    @Override
    public List<OrderModel> getOrdersByShopId(long shopId, Pageable pageable) {

        Page<Order> orders = orderRepository.findOrdersByShopId(shopId, pageable);
        if (null != orders && !orders.isEmpty()) {
            TypeToken<List<OrderModel>> typeToken = new TypeToken<>() {
            };
            return modelMapper.map(orders.getContent(), typeToken.getType());
        }
        return new ArrayList<>();
    }
}
