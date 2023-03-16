package com.example.customerserver.web.validation;

import com.example.customerserver.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class KeyCloakUniqueValidator implements ConstraintValidator<KeycloakUnique,String> {

    private final CustomerRepository customerRepository;
    private UniqueType uniqueType;

    @Override
    public void initialize(KeycloakUnique constraintAnnotation) {
        uniqueType = constraintAnnotation.uniqueType();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        switch (uniqueType){
            case USERNAME: return !customerRepository.existsCustomerByUsername(value);
            case EMAIL: return !customerRepository.existsCustomerByEmail(value);
        }
        throw new IllegalStateException("유일해야하는 키 값이 잘못들어왔습니다");
    }

}
