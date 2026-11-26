package com.example.bloodbank.util.annotation;

import com.example.bloodbank.util.validator.ExistsColumnValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistsColumnValidator.class )
public @interface ExistsColumn {
    Class<?> targetEntity();
    String column() default "id";
    String message() default "the value does not exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
