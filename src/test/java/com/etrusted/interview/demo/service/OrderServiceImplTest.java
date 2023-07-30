package com.etrusted.interview.demo.service;

import com.etrusted.interview.demo.entity.Order;
import com.etrusted.interview.demo.enums.PaymentType;
import com.etrusted.interview.demo.entity.Shop;
import com.etrusted.interview.demo.entity.User;
import com.etrusted.interview.demo.model.OrderModel;
import com.etrusted.interview.demo.model.ShopModel;
import com.etrusted.interview.demo.model.UserModel;
import com.etrusted.interview.demo.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    public OrderServiceImplTest() {
    }

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserService userService;

    @Mock
    private ShopService shopService;

    @InjectMocks
    private OrderServiceImpl orderService;


    @Test
    void createsOrderAndVerifiesOrderInputArguments() {
        OrderModel orderModel = createOrderRequest();

        User user = createUserResponse();
        when(userService.findUserByEmail(orderModel.getUser().getEmail())).thenReturn(Optional.of(user));
        Shop shop = createShopResponse();
        when(shopService.findShopByUrl(orderModel.getShop().getUrl())).thenReturn(Optional.of(shop));
        orderService.createOrder(orderModel);

        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository, times(1)).save(orderArgumentCaptor.capture());

        Order capturedOrder = orderArgumentCaptor.getValue();

        assertNotNull(capturedOrder);
        assertEquals(orderModel.getPaymentType(), capturedOrder.getPaymentType());
        assertEquals(createUserResponse().getEmail(), capturedOrder.getUser().getEmail());
        assertEquals(createShopResponse().getUrl(), capturedOrder.getShop().getUrl());
    }

    @Test
    void getsOrdersByOrderIdAndVerifiesOrderResponse() {
        final long orderId = 1L;
        OrderModel expectedOrderModel = createOrderModelResponse().get(0);
        Order orderEntity = createOrderEntityResponse().get(0);
        when(orderRepository.findById(any())).thenReturn(Optional.ofNullable(orderEntity));
        when(modelMapper.map(orderEntity, OrderModel.class)).thenReturn(expectedOrderModel);
        OrderModel orderResponse = orderService.getOrderByOrderId(orderId);
        assertNotNull(orderResponse);
        assertEquals(expectedOrderModel, orderResponse);
    }

    @Test
    void returnsEmptyOrderListWhenInvalidShopIdPassedToGetOrdersByShopId() {
        final long shopId = 5L;
        final int page = 0;
        final int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        List<Order> orderList = new ArrayList<>();
        Page<Order> emptyList = new PageImpl<>(orderList, pageable, 0);
        when(orderRepository.findOrdersByShopId(shopId, pageable)).thenReturn(emptyList);
        List<OrderModel> orderResponse = orderService.getOrdersByShopId(shopId, pageable);
        assertEquals(emptyList.getTotalElements(), orderResponse.size());
        assertTrue(emptyList.getContent().isEmpty());
    }

    @Test
    void getsOrdersByShopIdAndVerifiesResponse() {
        final long shopId = 1;
        final int page = 0;
        final int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        List<OrderModel> expectedOrderModelList = createOrderModelResponse();
        List<Order> orderList = createOrderEntityResponse();
        Page<Order> orderListPage = new PageImpl<>(orderList);
        when(orderRepository.findOrdersByShopId(shopId, pageable)).thenReturn(orderListPage);
        TypeToken<List<OrderModel>> typeToken = new TypeToken<>() {
        };
        when(modelMapper.map(orderListPage.getContent(), typeToken.getType())).thenReturn(expectedOrderModelList);
        List<OrderModel> orderModels = orderService.getOrdersByShopId(shopId, pageable);
        assertNotNull(orderModels);
        assertEquals(expectedOrderModelList, orderModels);
    }

    private OrderModel createOrderRequest() {
        return OrderModel.builder()
                .orderReference(UUID.randomUUID().toString())
                .user(UserModel.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .email("john.doe@example.com")
                        .address("Germany")
                        .build())
                .shop(ShopModel.builder()
                        .url("www.trustedshop.com")
                        .build())
                .paymentType(PaymentType.PAYPAL)
                .build();
    }

    private User createUserResponse() {
        return User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .address("Germany")
                .build();
    }

    private Shop createShopResponse() {
        return Shop.builder()
                .url("www.trustedshop.com")
                .build();
    }

    private List<Order> createOrderEntityResponse() {
        List<Order> orderEntities = new ArrayList<>();

        Order order1 = Order.builder()
                .id(1)
                .orderReference(UUID.randomUUID().toString())
                .user(User.builder()
                        .id(1)
                        .firstName("John")
                        .lastName("Doe")
                        .email("john.doe@example.com")
                        .address("Germany")
                        .build())
                .shop(Shop.builder()
                        .id(1L)
                        .url("www.trustedshop.com")
                        .build())
                .paymentType(PaymentType.PAYPAL)
                .build();

        Order order2 = Order.builder()
                .id(2)
                .orderReference(UUID.randomUUID().toString())
                .user(User.builder()
                        .id(2)
                        .firstName("Jane")
                        .lastName("Smith")
                        .email("jane.smith@example.com")
                        .address("USA")
                        .build())
                .shop(Shop.builder()
                        .id(2)
                        .url("www.bestshop.com")
                        .build())
                .paymentType(PaymentType.CREDIT_CARD)
                .build();

        orderEntities.add(order1);
        orderEntities.add(order2);
        return orderEntities;
    }

    private List<OrderModel> createOrderModelResponse() {
        List<OrderModel> orderModels = new ArrayList<>();
        OrderModel order1 = OrderModel.builder()
                .id(1)
                .orderReference(UUID.randomUUID().toString())
                .user(UserModel.builder()
                        .id(1)
                        .firstName("John")
                        .lastName("Doe")
                        .email("john.doe@example.com")
                        .address("Germany")
                        .build())
                .shop(ShopModel.builder()
                        .id(1L)
                        .url("www.trustedshop.com")
                        .build())
                .paymentType(PaymentType.PAYPAL)
                .build();

        OrderModel order2 = OrderModel.builder()
                .id(2)
                .orderReference(UUID.randomUUID().toString())
                .user(UserModel.builder()
                        .id(2)
                        .firstName("Jane")
                        .lastName("Smith")
                        .email("jane.smith@example.com")
                        .address("USA")
                        .build())
                .shop(ShopModel.builder()
                        .id(2)
                        .url("www.bestshop.com")
                        .build())
                .paymentType(PaymentType.CREDIT_CARD)
                .build();

        orderModels.add(order1);
        orderModels.add(order2);
        return orderModels;
    }

}
