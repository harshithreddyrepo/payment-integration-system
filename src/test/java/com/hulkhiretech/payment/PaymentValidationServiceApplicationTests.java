package com.hulkhiretech.payment;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "PAYMENT_SIGNATURE_SECRET=test-signing-secret")
class PaymentValidationServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
