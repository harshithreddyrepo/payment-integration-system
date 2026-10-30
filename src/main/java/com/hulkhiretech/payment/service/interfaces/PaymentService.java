package com.hulkhiretech.payment.service.interfaces;

import com.hulkhiretech.payment.pojo.PaymentReq;

public interface PaymentService {

	String createPayment(PaymentReq paymentReq);
}
