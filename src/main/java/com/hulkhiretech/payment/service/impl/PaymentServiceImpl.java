package com.hulkhiretech.payment.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.hulkhiretech.payment.constant.ValidatorRuleEnum;
import com.hulkhiretech.payment.pojo.PaymentReq;
import com.hulkhiretech.payment.service.interfaces.PaymentService;
import com.hulkhiretech.payment.service.interfaces.validator.BusinessValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

	private final ApplicationContext applicationContext;

	private final List<String> validatorCheckList;


	public PaymentServiceImpl(ApplicationContext applicationContext,
			@Value("${validator.check-list}") String validatorCheckList) {

		this.applicationContext = applicationContext;

		log.info("Validator check list: {}", validatorCheckList);

		this.validatorCheckList = List.of(validatorCheckList.split(","))
				.stream()
				.map(String::trim)
				.collect(Collectors.toList());
	}

	@Override
	public String createPayment(PaymentReq paymentReq) {
		log.info("PaymentServiceImpl.createPayment started");

		validatorCheckList.stream()
				.forEach(validatorName -> {
					Class<? extends BusinessValidator> validatorClass =
							ValidatorRuleEnum.getValidatorClass(validatorName);
					BusinessValidator validator = applicationContext.getBean(validatorClass);
					validator.validate(paymentReq);
					log.info("Executed validator: {}", validatorName);
				});

		log.info("PaymentServiceImpl.createPayment completed");
		return "Proceeding with payment creation";

	}

}
