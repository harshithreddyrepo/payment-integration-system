package com.hulkhiretech.payment.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hulkhiretech.payment.pojo.PaymentReq;
import com.hulkhiretech.payment.service.interfaces.HmacSHA256;

class PaymentServiceImplTest {

	private HmacSHA256 hmacSignatureGenerator;
	private PaymentServiceImpl paymentService;

	@BeforeEach
	void setUp() {
		hmacSignatureGenerator = mock(HmacSHA256.class);
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
		RuntimeException exception = assertThrows(
				RuntimeException.class,
				() -> paymentService.createPayment(null, "signature"));
		assertEquals("Payment request must not be null", exception.getMessage());
	}

	@Test
	void rejectsNonMatchingSignature() {
		PaymentReq paymentReq = new PaymentReq();
		String serializedRequest = "{\"user\":null,\"payment\":null}";
		doThrow(new RuntimeException("Invalid HMAC signature"))
				.when(hmacSignatureGenerator)
				.verifySignature(serializedRequest, "different-signature");

		assertThrows(
				RuntimeException.class,
				() -> paymentService.createPayment(paymentReq, "different-signature"));
		verify(hmacSignatureGenerator)
				.verifySignature(serializedRequest, "different-signature");
	}
}
