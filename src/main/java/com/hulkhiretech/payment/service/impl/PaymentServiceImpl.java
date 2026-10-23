package com.hulkhiretech.payment.service.impl;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hulkhiretech.payment.pojo.PaymentReq;
import com.hulkhiretech.payment.service.interfaces.PaymentService;
import com.hulkhiretech.payment.util.interfaces.HmacSHA256Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

	private final ObjectMapper objectMapper;
	private final HmacSHA256Util hmacSignatureGenerator;

	public PaymentServiceImpl(
			ObjectMapper objectMapper,
			HmacSHA256Util hmacSignatureGenerator) {
		this.objectMapper = objectMapper;
		this.hmacSignatureGenerator = hmacSignatureGenerator;
	}

	@Override
	public String createPayment(PaymentReq paymentReq, String incomingSignature) {
		log.info("PaymentService.createPayment started");
		try {
			if (paymentReq == null) {
				throw new IllegalArgumentException("Payment request must not be null");
			}

			String serializedRequest = serializeRequest(paymentReq);
			hmacSignatureGenerator.verifySignature(serializedRequest, incomingSignature);
			return "HMAC signature validation successful";
		} finally {
			log.info("PaymentService.createPayment ended");
		}
	}

	private String serializeRequest(PaymentReq paymentReq) {
		try {
			return objectMapper.writeValueAsString(paymentReq);
		} catch (JsonProcessingException exception) {
			log.error("Unable to serialize payment request for signature verification", exception);
			throw new IllegalStateException("Unable to serialize payment request", exception);
		}
	}
}
