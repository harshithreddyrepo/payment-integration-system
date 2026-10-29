package com.hulkhiretech.payment.service.impl;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hulkhiretech.payment.pojo.PaymentReq;
import com.hulkhiretech.payment.service.interfaces.HmacSHA256;
import com.hulkhiretech.payment.service.interfaces.PaymentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

	private final ObjectMapper objectMapper;
	private final HmacSHA256 hmacSignatureGenerator;

	public PaymentServiceImpl(
			ObjectMapper objectMapper,
			HmacSHA256 hmacSignatureGenerator) {
		this.objectMapper = objectMapper;
		this.hmacSignatureGenerator = hmacSignatureGenerator;
	}

	@Override
	public String createPayment(PaymentReq paymentReq, String incomingSignature) {
		log.info("PaymentServiceImpl.createPayment started");

		// TODO

		log.info("PaymentServiceImpl.createPayment completed");
		return "Proceeding with payment creation";

	}

}
