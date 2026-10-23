package com.hulkhiretech.payment.controller;

import com.hulkhiretech.payment.pojo.PaymentReq;
import com.hulkhiretech.payment.service.interfaces.PaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("v1/payments")
public class PaymentController {

	private final PaymentService paymentService;

	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@PostMapping
	public String createPayment(
			@RequestHeader("HMAC-signature") String hmacSignature,
			@RequestBody PaymentReq paymentReq) {
		log.info("PaymentController.createPayment started");
		try {
			return paymentService.createPayment(paymentReq, hmacSignature);
		} finally {
			log.info("PaymentController.createPayment ended");
		}
	}
}
