package com.hulkhiretech.payment.controller;

import com.hulkhiretech.payment.service.interfaces.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PaymentControllerTest {

	private PaymentService paymentService;
	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		paymentService = mock(PaymentService.class);
		mockMvc = MockMvcBuilders.standaloneSetup(new PaymentController(paymentService)).build();
	}

	@Test
	void createPaymentReturnsValidationSuccessMessage() throws Exception {
		when(paymentService.createPayment(any()))
				.thenReturn("Proceeding with payment creation");

		mockMvc.perform(post("/v1/payments")
						.header("HMAC-signature", "client-provided-signature")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "user": {
								    "firstName": "Alex",
								    "email": "alex@example.com"
								  },
								  "payment": {
								    "totalAmount": 1250,
								    "currency": "USD"
								  }
								}
								"""))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
				.andExpect(content().string("Proceeding with payment creation"));

		verify(paymentService).createPayment(any());
	}

	@Test
	void createPaymentPropagatesServiceAuthorizationException() throws Exception {
		when(paymentService.createPayment(any()))
				.thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid HMAC signature"));

		mockMvc.perform(post("/v1/payments")
						.header("HMAC-signature", "invalid-signature")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "user": {
								    "firstName": "Alex",
								    "email": "alex@example.com"
								  },
								  "payment": {
								    "totalAmount": 1250,
								    "currency": "USD"
								  }
								}
								"""))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void createPaymentRequiresHmacSignatureHeader() throws Exception {
		mockMvc.perform(post("/v1/payments")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "user": {
								    "firstName": "Alex",
								    "email": "alex@example.com"
								  },
								  "payment": {
								    "totalAmount": 1250,
								    "currency": "USD"
								  }
								}
								"""))
				.andExpect(status().isBadRequest());

		verifyNoInteractions(paymentService);
	}
}
