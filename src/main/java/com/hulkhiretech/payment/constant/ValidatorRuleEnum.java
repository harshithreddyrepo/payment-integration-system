package com.hulkhiretech.payment.constant;

import com.hulkhiretech.payment.service.impl.validator.ValidatorCheck1;
import com.hulkhiretech.payment.service.impl.validator.ValidatorCheck2;
import com.hulkhiretech.payment.service.impl.validator.ValidatorCheck3;
import com.hulkhiretech.payment.service.interfaces.validator.BusinessValidator;

public enum ValidatorRuleEnum {
	VALIDATOR_CHECK1(ValidatorCheck1.class),
	VALIDATOR_CHECK2(ValidatorCheck2.class),
	VALIDATOR_CHECK3(ValidatorCheck3.class);

	private final Class<? extends BusinessValidator> validatorClass;

	ValidatorRuleEnum(Class<? extends BusinessValidator> validatorClass) {
		this.validatorClass = validatorClass;
	}

	public Class<? extends BusinessValidator> getValidatorClass() {
		return validatorClass;
	}

	public static Class<? extends BusinessValidator> getValidatorClass(String validatorName) {
		for (ValidatorRuleEnum validatorRule : values()) {
			if (validatorRule.name().equals(validatorName)) {
				return validatorRule.getValidatorClass();
			}
		}

		throw new RuntimeException("Unknown validator: " + validatorName);
	}
}
