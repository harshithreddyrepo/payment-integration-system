package com.hulkhiretech.payment.pojo;

import java.util.List;

import lombok.Data;

@Data
public class PaymentReq {

    private User user;
    private Payment payment;

    @Data
    public static class User {
        private String endUserId;
        private String firstName;
        private String lastName;
        private String email;
        private String phn;
        private String country;
        private String locale;
    }

    @Data
    public static class Payment {
        private Integer totalAmount;
        private String currency;
        private String paymentType;       // SALE
        private String paymentMethod;     // APM
        private String provider;          // STRIPE
        private String merchantTxnRef;

        private String cancelUrl;
        private String successUrl;
        private List<LineItem> lineItems;
    }

    @Data
    public static class LineItem {
        private Integer quantity;
        private String currency;
        private String productName;
        private Long unitAmount;
    }
}

