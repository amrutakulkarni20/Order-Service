package com.etrusted.interview.demo.enums;

public enum PaymentType {
  PAYPAL("PAYPAL"),
  CREDIT_CARD("CREDIT_CARD"),
  CASH_ON_DELIVERY("CASH_ON_DELIVERY");

  public final String typeName;

  PaymentType(String typeName) {
    this.typeName = typeName;
  }
}
