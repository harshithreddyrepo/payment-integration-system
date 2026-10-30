package com.hulkhiretech.payment.service.impl.validator;

import org.springframework.stereotype.Service;

import com.hulkhiretech.payment.pojo.PaymentReq;
import com.hulkhiretech.payment.service.interfaces.validator.BusinessValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ValidatorCheck2 implements BusinessValidator {

	@Override
	public void validate(PaymentReq paymentReq) {
		log.info("ValidatorCheck2.validate called");
	}
}
