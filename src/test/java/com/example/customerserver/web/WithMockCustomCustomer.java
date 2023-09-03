package com.example.customerserver.web;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomCustomerSecurityContextFactory.class)
public @interface WithMockCustomCustomer {

	String username() default "rob";

	String name() default "Rob Winch";

	String role() default "USER";

	long id() default 1L;
}
