package com.hulkhiretech.payment.service.impl;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hulkhiretech.payment.service.interfaces.HmacSHA256;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HmacSHA256Impl implements HmacSHA256 {

	private static final String ALGORITHM = "HmacSHA256";

	private final String signingSecret;

	public HmacSHA256Impl(@Value("${PAYMENT_SIGNATURE_SECRET:${payment.signature.secret}}") String signingSecret) {
		if (signingSecret == null || signingSecret.isEmpty()) {
			throw new RuntimeException("Payment signature secret must not be null or empty");
		}
		this.signingSecret = signingSecret;
	}

	@Override
	public String generateHmacSignature(String data) {
		log.debug("HmacSHA256Util.generateHmacSignature started");
		if (data == null) {
			throw new RuntimeException("Data must not be null");
		}

		try {
			// Mac instances maintain mutable state, so create one for each invocation.
			Mac mac = Mac.getInstance(ALGORITHM);
			SecretKeySpec secretKey = new SecretKeySpec(
					signingSecret.getBytes(StandardCharsets.UTF_8),
					ALGORITHM);
			mac.init(secretKey);

			byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
			byte[] signatureBytes = mac.doFinal(dataBytes);
			String signature = Base64.getEncoder().encodeToString(signatureBytes);
			log.info("HMAC-SHA256 input: {}", data);

			log.info("generated HMAC-signature: {}", signature);

			log.info("HmacSHA256Util.generateHmacSignature completed");
			return signature;
		} catch (GeneralSecurityException exception) {
			log.error("HmacSHA256Util.generateHmacSignature failed", exception);
			throw new RuntimeException("Unable to generate HMAC-SHA256 signature", exception);
		}
	}

	@Override
	public void verifySignature(String data, String signature) {
		if (data == null) {
			throw new RuntimeException("Data must not be null");
		}
		if (signature == null || signature.isEmpty()) {
			throw new RuntimeException("Invalid HMAC signature");
		}

		String expectedSignature = generateHmacSignature(data);
		boolean signaturesMatch = MessageDigest.isEqual(
				expectedSignature.getBytes(StandardCharsets.US_ASCII),
				signature.getBytes(StandardCharsets.US_ASCII));
		if (!signaturesMatch) {
			log.info("HMAC-Signature validation failed for request to {}");
			throw new RuntimeException("Invalid HMAC signature");
		}
	}
}
