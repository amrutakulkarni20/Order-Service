package com.etrusted.interview.demo.controller;

import com.etrusted.interview.demo.model.OrderModel;
import com.etrusted.interview.demo.model.OrderResponseModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Tag(name = "Order API Service", description = "The Order API Microservice exposes REST endpoints to create and track the order.")
public interface IOrderController {

    @Operation(summary = "Creates new Order", description = "This API creates new Order.")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Order created successfully.")})
    OrderResponseModel createOrder(OrderModel order);

    @Operation(summary = "Gets Order by Order Id", description = "This API returns Order by Order Id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Order returned successfully.")})
    OrderModel getOrderByOrderId(long orderId);

    @Operation(summary = "Gets Orders by Shop Id", description = "This API returns Orders by Shop Id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Orders returned successfully.")})
    List<OrderModel> getOrdersByShopId(long shopId, Pageable pageable);
}
