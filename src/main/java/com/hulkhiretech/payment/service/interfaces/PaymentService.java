package com.hulkhiretech.payment.service.interfaces;

import com.hulkhiretech.payment.pojo.PaymentReq;

public interface PaymentService {

	PaymentReq createPayment(PaymentReq paymentReq);
}
