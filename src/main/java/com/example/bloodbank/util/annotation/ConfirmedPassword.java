package com.example.bloodbank.util.annotation;

import com.example.bloodbank.util.validator.ConfirmedPasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ConfirmedPasswordValidator.class)
public @interface ConfirmedPassword {
    String confirmedField() default "confirmedPassword";
    String message() default "password and confirmation do not match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
