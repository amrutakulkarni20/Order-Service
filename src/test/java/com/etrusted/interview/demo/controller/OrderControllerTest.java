package com.etrusted.interview.demo.controller;

import com.etrusted.interview.demo.DemoApplication;
import com.etrusted.interview.demo.enums.PaymentType;
import com.etrusted.interview.demo.exception.ErrorCode;
import com.etrusted.interview.demo.exception.ErrorDetails;
import com.etrusted.interview.demo.model.OrderModel;
import com.etrusted.interview.demo.model.OrderResponseModel;
import com.etrusted.interview.demo.model.ShopModel;
import com.etrusted.interview.demo.model.UserModel;
import com.etrusted.interview.demo.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = {DemoApplication.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class OrderControllerTest {

    private HttpHeaders httpHeaders;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${server.port}")
    private String testPort;

    @Value("${application.test.host}")
    private String host;


    @Test
    void createsOrderAndVerifiesOrderId(){
        ResponseEntity<OrderResponseModel> orderResponse = postOrder();
        assertEquals(1, orderResponse.getBody().getOrderId());
    }

    @Test
    void getsOrderByOrderIdAndVerifiesOrderId(){
        postOrder();
        final long orderId = 1;
        ResponseEntity<OrderModel> order = testRestTemplate.exchange(createRequestURL("/order/" + orderId, host, testPort),
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        assertNotNull(order);
        assertSame(orderId,order.getBody().getId());
    }

    @Test
    void getOrdersByShopIdAndVerifiesShopId(){
        postOrder();
        final long shopId = 1;
        final int page = 0;
        final int size = 5;

        ResponseEntity<List<OrderModel>> order = testRestTemplate.exchange(createRequestURL("/orders/" + shopId, host, testPort) + "?page=" + page + "&size=" + size,
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertNotNull(order);
        assertNotNull(order.getBody());
        assertFalse(order.getBody().isEmpty());
        OrderModel orderModel = order.getBody().get(0);
        assertNotNull(orderModel.getShop());
        assertEquals(shopId,orderModel.getShop().getId());
    }

    private ResponseEntity<OrderResponseModel> postOrder(){
        OrderModel orderRequest = OrderModel.builder()
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

        HttpEntity<OrderModel> orderModelHttpEntity = new HttpEntity<>(orderRequest,null);
        return testRestTemplate.exchange(createRequestURL("/order", host, testPort),
                HttpMethod.POST, orderModelHttpEntity, new ParameterizedTypeReference<>() {});
    }

    @Test
    void throwsInvalidRequestExceptionWhenOrderReferenceIsMissing() {
        OrderModel orderRequest = createInvalidOrderRequestWithoutOrderReference();
        HttpEntity<OrderModel> orderModelHttpEntity = new HttpEntity<>(orderRequest,null);
        ResponseEntity<ErrorDetails> errorResponse = testRestTemplate.exchange(createRequestURL("/order", host, testPort),
                HttpMethod.POST, orderModelHttpEntity, new ParameterizedTypeReference<>() {});
        assertEquals(ErrorCode.INVALID_REQUEST, errorResponse.getBody().getCode());
        String actualErrorMessage = errorResponse.getBody().getMessage();
        assertEquals("Input request validation failed", actualErrorMessage);
    }

    private OrderModel  createInvalidOrderRequestWithoutOrderReference() {
        UserModel user = UserModel.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .address("Germany")
                .build();

        ShopModel shop = ShopModel.builder()
                .url("www.trustedshop.com")
                .build();

        return OrderModel.builder()
                .user(user)
                .shop(shop)
                .paymentType(PaymentType.PAYPAL)
                .build();
    }

    @Test
    void throwsInvalidRequestExceptionWhenShopUrlIsMissing() {
        OrderModel orderRequest = createInvalidOrderRequestWithoutShopUrl();
        HttpEntity<OrderModel> orderModelHttpEntity = new HttpEntity<>(orderRequest,null);
        ResponseEntity<ErrorDetails> errorResponse = testRestTemplate.exchange(createRequestURL("/order", host, testPort),
                HttpMethod.POST, orderModelHttpEntity, new ParameterizedTypeReference<>() {});
        assertEquals(ErrorCode.INVALID_REQUEST, errorResponse.getBody().getCode());
        String actualErrorMessage = errorResponse.getBody().getMessage();
        assertEquals("Input request validation failed", actualErrorMessage);
    }

    private OrderModel createInvalidOrderRequestWithoutShopUrl() {
        UserModel user = UserModel.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .address("Germany")
                .build();

        ShopModel shop = ShopModel.builder().url(null).build();

        return OrderModel.builder()
                .user(user)
                .shop(shop)
                .orderReference(UUID.randomUUID().toString())
                .paymentType(PaymentType.PAYPAL)
                .build();
    }

    @Test
    void throwsInvalidRequestExceptionWhenPaymentTypeIsCashOnDeliveryAndAddressIsEmpty(){
        OrderModel orderRequest = createInvalidOrderRequestWithoutAddress();
        HttpEntity<OrderModel> orderModelHttpEntity = new HttpEntity<>(orderRequest,null);
        ResponseEntity<ErrorDetails> errorResponse = testRestTemplate.exchange(createRequestURL("/order", host, testPort),
                HttpMethod.POST, orderModelHttpEntity, new ParameterizedTypeReference<>() {});
        assertEquals(ErrorCode.INVALID_REQUEST, errorResponse.getBody().getCode());
        String actualErrorMessage = errorResponse.getBody().getMessage();
        assertEquals("Input request validation failed", actualErrorMessage);
    }

    private OrderModel createInvalidOrderRequestWithoutAddress() {
        UserModel user = UserModel.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        ShopModel shop = ShopModel.builder().url("www.trustedshop.com").build();

        return OrderModel.builder()
                .user(user)
                .shop(shop)
                .orderReference(UUID.randomUUID().toString())
                .paymentType(PaymentType.CASH_ON_DELIVERY)
                .build();
    }

    @Test
    void createsOrderWithExistingUserEmailAndVerifiesUserId(){
        ResponseEntity<OrderResponseModel> existingOrderResponse = postOrder();
        ResponseEntity<OrderModel> existingOrder = testRestTemplate.exchange(createRequestURL("/order/" + existingOrderResponse.getBody().getOrderId(), host, testPort),
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        long existingUserId = existingOrder.getBody().getUser().getId();
        String existingEmail = existingOrder.getBody().getUser().getEmail();

        ResponseEntity<OrderResponseModel> newOrderResponse = postOrder();
        ResponseEntity<OrderModel> newOrder = testRestTemplate.exchange(createRequestURL("/order/" + newOrderResponse.getBody().getOrderId(), host, testPort),
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        long newUserId = newOrder.getBody().getUser().getId();
        String newEmail = newOrder.getBody().getUser().getEmail();

        assertSame(existingUserId,newUserId);
        assertEquals(existingEmail,newEmail);
    }

    @Test
    void throwsInvalidRequestExceptionWhenUserEmailIsMissing() {
        OrderModel orderRequest = createInvalidOrderRequestWithoutUserEmail();
        HttpEntity<OrderModel> orderModelHttpEntity = new HttpEntity<>(orderRequest,null);
        ResponseEntity<ErrorDetails> errorResponse = testRestTemplate.exchange(createRequestURL("/order", host, testPort),
                HttpMethod.POST, orderModelHttpEntity, new ParameterizedTypeReference<>() {});
        assertEquals(ErrorCode.INVALID_REQUEST, errorResponse.getBody().getCode());
        String actualErrorMessage = errorResponse.getBody().getMessage();
        assertEquals("Input request validation failed", actualErrorMessage);
    }

    private OrderModel createInvalidOrderRequestWithoutUserEmail() {
       UserModel user = UserModel.builder()
                .firstName("John")
                .lastName("Doe")
                .address("Germany")
                .build();

       ShopModel shop = ShopModel.builder().url("www.trustedshop.com").build();

       return OrderModel.builder()
                .user(user)
                .shop(shop)
                .orderReference(UUID.randomUUID().toString())
                .paymentType(PaymentType.PAYPAL)
                .build();
    }
    private String createRequestURL(final String uri, final String host,
                                    final String port) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://")
                .append(host)
                .append(":")
                .append(port)
                .append(uri);
        return stringBuilder.toString();
    }
}
