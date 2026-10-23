package com.hulkhiretech.payment.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hulkhiretech.payment.exception.InvalidHmacSignatureException;
import com.hulkhiretech.payment.pojo.PaymentReq;
import com.hulkhiretech.payment.util.interfaces.HmacSHA256Util;

class PaymentServiceImplTest {

	private HmacSHA256Util hmacSignatureGenerator;
	private PaymentServiceImpl paymentService;

	@BeforeEach
	void setUp() {
		hmacSignatureGenerator = mock(HmacSHA256Util.class);
		paymentService = new PaymentServiceImpl(new ObjectMapper(), hmacSignatureGenerator);
	}

	@Test
	void verifiesSignatureAndReturnsSuccessMessage() {
		PaymentReq paymentReq = new PaymentReq();
		String incomingSignature = "sample-base64-signature";
		String serializedRequest = "{\"user\":null,\"payment\":null}";

		String response = paymentService.createPayment(paymentReq, incomingSignature);

		assertEquals("HMAC signature validation successful", response);
		verify(hmacSignatureGenerator)
				.verifySignature(serializedRequest, incomingSignature);
	}

	@Test
	void rejectsNullPaymentRequest() {
		assertThrows(
				IllegalArgumentException.class,
				() -> paymentService.createPayment(null, "signature"));
	}

	@Test
	void rejectsNonMatchingSignature() {
		PaymentReq paymentReq = new PaymentReq();
		String serializedRequest = "{\"user\":null,\"payment\":null}";
		doThrow(new InvalidHmacSignatureException())
				.when(hmacSignatureGenerator)
				.verifySignature(serializedRequest, "different-signature");

		assertThrows(
				InvalidHmacSignatureException.class,
				() -> paymentService.createPayment(paymentReq, "different-signature"));
		verify(hmacSignatureGenerator)
				.verifySignature(serializedRequest, "different-signature");
	}
}
