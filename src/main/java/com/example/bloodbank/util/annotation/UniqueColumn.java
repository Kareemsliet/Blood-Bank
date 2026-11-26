package com.example.bloodbank.util.annotation;

import com.example.bloodbank.util.validator.UniqueColumnValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention((RetentionPolicy.RUNTIME))
@Constraint(validatedBy = UniqueColumnValidator.class)
public @interface UniqueColumn {
    Class<?> targetEntity();
    String column();
    String exceptColumn() default "id";
    String exceptPathVariable() default "id";
    String[] exceptPathMethods() default "PATCH";
    String message() default "the value already taken";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
