package com.hulkhiretech.payment.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.hulkhiretech.payment.service.impl.HmacSHA256Impl;
import com.hulkhiretech.payment.service.interfaces.HmacSHA256;

import org.junit.jupiter.api.Test;

class HmacSHA256UtilTest {

	@Test
	void generatesHmacSha256AsBase64() {
		String data = "what do ya want for nothing?";
		HmacSHA256Impl hmacSignatureGenerator = new HmacSHA256Impl("Jefe");

		String signature = hmacSignatureGenerator.generateHmacSignature(data);

		assertEquals("W9zBRr9gdU5qBCQmCJV1x1oAPwidJzmDnexYuWTsOEM=", signature);
	}

	@Test
	void allowsEmptyData() {
		HmacSHA256Impl hmacSignatureGenerator = new HmacSHA256Impl("Jefe");
		String signature = hmacSignatureGenerator.generateHmacSignature("");

		assertEquals("kjWYym1krypdunnc0CGooP5cX1V1Ga2q8K1TLUUG3TA=", signature);
	}

	@Test
	void rejectsNullData() {
		HmacSHA256Impl hmacSignatureGenerator = new HmacSHA256Impl("test-secret");
		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> hmacSignatureGenerator.generateHmacSignature(null));
		assertEquals("Data must not be null", exception.getMessage());
	}

	@Test
	void verifiesMatchingSignature() {
		String data = "{\"paymentId\":\"test-123\"}";
		HmacSHA256Impl hmacSignatureGenerator = new HmacSHA256Impl("test-secret");
		String signature = hmacSignatureGenerator.generateHmacSignature(data);

		hmacSignatureGenerator.verifySignature(data, signature);
	}

	@Test
	void rejectsMismatchedOrMissingSignature() {
		HmacSHA256Impl hmacSignatureGenerator = new HmacSHA256Impl("test-secret");
		String requestData = "{\"paymentId\":\"test-123\"}";
		RuntimeException mismatchedSignature = assertThrows(
				RuntimeException.class,
				() -> hmacSignatureGenerator.verifySignature(requestData, "bad-signature"));
		assertEquals("Invalid HMAC signature", mismatchedSignature.getMessage());

		RuntimeException missingSignature = assertThrows(
				RuntimeException.class,
				() -> hmacSignatureGenerator.verifySignature(requestData, null));
		assertEquals("Invalid HMAC signature", missingSignature.getMessage());
	}
}
