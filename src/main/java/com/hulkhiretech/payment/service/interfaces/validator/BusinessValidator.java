package com.hulkhiretech.payment.service.interfaces.validator;

import com.hulkhiretech.payment.pojo.PaymentReq;

public interface BusinessValidator {

	void validate(PaymentReq paymentReq);
}
