package com.example.customerserver.web.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD, ElementType.METHOD
	, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = KeyCloakUniqueValidator.class)
@Documented
public @interface KeycloakUnique {

	Class<? extends Payload>[] payload() default {};

	Class<?>[] groups() default {};

	UniqueType uniqueType() default UniqueType.USERNAME;

	String message() default "이미 등록된 아이디입니다.";
}

