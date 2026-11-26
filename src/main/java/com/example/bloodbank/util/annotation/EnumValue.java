package com.example.bloodbank.util.annotation;

import com.example.bloodbank.util.validator.EnumValueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(EnumValue.List.class)
@Constraint(validatedBy = EnumValueValidator.class)
public @interface EnumValue {
    Class<? extends Enum<?>> targetEnum();
    String fieldName() default "";
    String message() default "the value does not match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        EnumValue[] value();
    }
}
