package com.hulkhiretech.payment.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

import com.hulkhiretech.payment.pojo.PaymentReq;
import com.hulkhiretech.payment.service.impl.validator.ValidatorCheck1;
import com.hulkhiretech.payment.service.impl.validator.ValidatorCheck2;
import com.hulkhiretech.payment.service.impl.validator.ValidatorCheck3;

class PaymentServiceImplTest {

	private ApplicationContext applicationContext;
	private PaymentServiceImpl paymentService;
	private ValidatorCheck1 validatorCheck1;
	private ValidatorCheck2 validatorCheck2;
	private ValidatorCheck3 validatorCheck3;

	@BeforeEach
	void setUp() {
		applicationContext = mock(ApplicationContext.class);
		validatorCheck1 = mock(ValidatorCheck1.class);
		validatorCheck2 = mock(ValidatorCheck2.class);
		validatorCheck3 = mock(ValidatorCheck3.class);
		when(applicationContext.getBean(ValidatorCheck1.class)).thenReturn(validatorCheck1);
		when(applicationContext.getBean(ValidatorCheck2.class)).thenReturn(validatorCheck2);
		when(applicationContext.getBean(ValidatorCheck3.class)).thenReturn(validatorCheck3);
		paymentService = new PaymentServiceImpl(applicationContext,
				"VALIDATOR_CHECK1, VALIDATOR_CHECK2, VALIDATOR_CHECK3");
	}

	@Test
	void returnsSuccessMessage() {
		PaymentReq paymentReq = new PaymentReq();

		String response = paymentService.createPayment(paymentReq);

		assertEquals("Proceeding with payment creation", response);
		verify(applicationContext).getBean(ValidatorCheck1.class);
		verify(applicationContext).getBean(ValidatorCheck2.class);
		verify(applicationContext).getBean(ValidatorCheck3.class);
		verify(validatorCheck1).validate(paymentReq);
		verify(validatorCheck2).validate(paymentReq);
		verify(validatorCheck3).validate(paymentReq);
	}
}
