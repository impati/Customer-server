package com.example.customerserver.web.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.example.customerserver.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KeyCloakUniqueValidator implements ConstraintValidator<KeycloakUnique, String> {

	private final CustomerRepository customerRepository;
	private UniqueType uniqueType;

	@Override
	public void initialize(KeycloakUnique constraintAnnotation) {
		uniqueType = constraintAnnotation.uniqueType();
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		return switch (uniqueType) {
			case USERNAME -> !customerRepository.existsCustomerByUsername(value);
			case EMAIL -> !customerRepository.existsCustomerByEmail(value);
		};
	}
}
