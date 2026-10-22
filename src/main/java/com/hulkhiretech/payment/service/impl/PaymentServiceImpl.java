package com.hulkhiretech.payment.service.impl;

import com.hulkhiretech.payment.service.interfaces.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

	@Override
	public String createPayment() {
		log.info("PaymentService.createPayment started");
		try {
			return UUID.randomUUID().toString();
		} finally {
			log.info("PaymentService.createPayment ended");
		}
	}
}
