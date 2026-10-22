package com.hulkhiretech.payment.controller;

import com.hulkhiretech.payment.service.interfaces.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
	void createPaymentReturnsRequestAsJsonObject() throws Exception {
		when(paymentService.createPayment(any())).thenAnswer(invocation -> invocation.getArgument(0));

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
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.user.firstName").value("Alex"))
				.andExpect(jsonPath("$.user.email").value("alex@example.com"))
				.andExpect(jsonPath("$.payment.totalAmount").value(1250))
				.andExpect(jsonPath("$.payment.currency").value("USD"));
	}
}
