package com.hulkhiretech.payment.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.hulkhiretech.payment.exception.InvalidHmacSignatureException;
import com.hulkhiretech.payment.util.impl.HmacSHA256UtilImpl;
import com.hulkhiretech.payment.util.interfaces.HmacSHA256Util;

import org.junit.jupiter.api.Test;

class HmacSHA256UtilTest {

	@Test
	void generatesHmacSha256AsBase64() {
		String data = "what do ya want for nothing?";
		HmacSHA256UtilImpl hmacSignatureGenerator = new HmacSHA256UtilImpl("Jefe");

		String signature = hmacSignatureGenerator.generateHmacSignature(data);

		assertEquals("W9zBRr9gdU5qBCQmCJV1x1oAPwidJzmDnexYuWTsOEM=", signature);
	}

	@Test
	void allowsEmptyData() {
		HmacSHA256UtilImpl hmacSignatureGenerator = new HmacSHA256UtilImpl("Jefe");
		String signature = hmacSignatureGenerator.generateHmacSignature("");

		assertEquals("kjWYym1krypdunnc0CGooP5cX1V1Ga2q8K1TLUUG3TA=", signature);
	}

	@Test
	void rejectsNullData() {
		HmacSHA256UtilImpl hmacSignatureGenerator = new HmacSHA256UtilImpl("test-secret");
		assertThrows(IllegalArgumentException.class,
				() -> hmacSignatureGenerator.generateHmacSignature(null));
	}

	@Test
	void verifiesMatchingSignature() {
		String data = "{\"paymentId\":\"test-123\"}";
		HmacSHA256UtilImpl hmacSignatureGenerator = new HmacSHA256UtilImpl("test-secret");
		String signature = hmacSignatureGenerator.generateHmacSignature(data);

		hmacSignatureGenerator.verifySignature(data, signature);
	}

	@Test
	void rejectsMismatchedOrMissingSignature() {
		HmacSHA256UtilImpl hmacSignatureGenerator = new HmacSHA256UtilImpl("test-secret");
		String requestData = "{\"paymentId\":\"test-123\"}";
		assertThrows(
				InvalidHmacSignatureException.class,
				() -> hmacSignatureGenerator.verifySignature(requestData, "bad-signature"));
		assertThrows(
				InvalidHmacSignatureException.class,
				() -> hmacSignatureGenerator.verifySignature(requestData, null));
	}
}
