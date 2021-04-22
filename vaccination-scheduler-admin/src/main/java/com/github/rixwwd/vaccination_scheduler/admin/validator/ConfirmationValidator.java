package com.github.rixwwd.vaccination_scheduler.admin.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

public class ConfirmationValidator implements ConstraintValidator<Confirmation, Object> {

	private String field;

	private String confirmationField;

	@Override
	public void initialize(Confirmation constraintAnnotation) {

		field = constraintAnnotation.field();
		confirmationField = constraintAnnotation.confirmationField();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {

		var wrapper = new BeanWrapperImpl(value);

		var v1 = wrapper.getPropertyValue(field);
		var v2 = wrapper.getPropertyValue(confirmationField);

		if (v1 == null && v2 == null) {
			return true;
		}

		if (v1 != null && v1.equals(v2)) {
			return true;
		} else {
			context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
					.addPropertyNode(confirmationField).addConstraintViolation();

			return false;
		}
	}

}
