package com.hulkhiretech.payment.util.interfaces;

public interface HmacSHA256Util {

	/**
	 * Generates an HMAC-SHA256 signature using UTF-8 encoded input and the
	 * configured signing secret.
	 *
	 * @param data the input data; must not be {@code null}
	 * @return the signature encoded as Base64
	 * @throws IllegalArgumentException if data is {@code null}
	 */
	String generateHmacSignature(String data);

	/**
	 * Verifies a Base64-encoded HMAC-SHA256 signature without a
	 * timing-sensitive string comparison.
	 *
	 * @param data the serialized request data
	 * @param signature the supplied Base64 signature
	 * @throws IllegalArgumentException if data is {@code null}
	 * @throws com.hulkhiretech.payment.exception.InvalidHmacSignatureException
	 *         if the supplied signature is missing or does not match
	 */
	void verifySignature(String data, String signature);
}
