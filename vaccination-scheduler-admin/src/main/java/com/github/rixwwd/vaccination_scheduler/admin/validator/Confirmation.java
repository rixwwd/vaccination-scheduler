package com.github.rixwwd.vaccination_scheduler.admin.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ConfirmationValidator.class)
public @interface Confirmation {

	String message() default "{com.github.rixwwd.vaccination_scheduler.admin.validator.Confirmation.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String field();
	
	String confirmationField();
}
