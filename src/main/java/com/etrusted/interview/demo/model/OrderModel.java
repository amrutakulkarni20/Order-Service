package com.etrusted.interview.demo.model;

import com.etrusted.interview.demo.enums.PaymentType;
import com.etrusted.interview.demo.validation.ConditionalValidation;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ConditionalValidation(selected = "paymentType", values = {"CASH_ON_DELIVERY"}, required = {"user.address"})
public class OrderModel {

    private long id;

    @NotBlank(message = "orderReference cannot be left blank")
    private String orderReference;

    @Valid
    private UserModel user;

    @Valid
    private ShopModel shop;

    @NotNull(message = "paymentType cannot be left blank")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    public OrderModel(String orderReference, UserModel user, ShopModel shop, PaymentType paymentType) {
        this.orderReference = orderReference;
        this.user = user;
        this.shop = shop;
        this.paymentType = paymentType;
    }
}
