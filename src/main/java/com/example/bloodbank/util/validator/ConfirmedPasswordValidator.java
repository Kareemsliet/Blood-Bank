package com.example.bloodbank.util.validator;

import com.example.bloodbank.util.annotation.ConfirmedPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Objects;

public class ConfirmedPasswordValidator implements ConstraintValidator<ConfirmedPassword, Object> {

    private String confirmedField;

    @Override
    public void initialize(ConfirmedPassword constraintAnnotation) {
        confirmedField = constraintAnnotation.confirmedField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        if (value == null) {
            return false;
        }

        BeanWrapper beanWrapper = new BeanWrapperImpl(value);
        if (!beanWrapper.isReadableProperty("password")) {
            throw new IllegalArgumentException("Object must have a readable 'password' property");
        }

        if (!beanWrapper.isReadableProperty(confirmedField)) {
            throw new IllegalArgumentException(
                    "Object must have a readable '" + confirmedField + "' property"
            );
        }

        Object password = beanWrapper.getPropertyValue("password");
        Object confirmedPassword = beanWrapper.getPropertyValue(confirmedField);
        boolean matches = Objects.equals(password, confirmedPassword);

        if (!matches) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(confirmedField)
                    .addConstraintViolation();
        }

        return matches;
    }
}
