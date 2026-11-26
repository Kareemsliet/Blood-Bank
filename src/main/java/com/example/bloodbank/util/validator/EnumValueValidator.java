package com.example.bloodbank.util.validator;

import com.example.bloodbank.util.annotation.EnumValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class EnumValueValidator implements ConstraintValidator<EnumValue,Object> {

    private final Set<Object> values = new HashSet<>();

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        Class<? extends Enum<?>> targetEnum = constraintAnnotation.targetEnum();
        String fieldName = constraintAnnotation.fieldName();

        for (Enum<?> enumConstant : targetEnum.getEnumConstants()) {
            if (fieldName == null || fieldName.isBlank()) {
                values.add(enumConstant.name());
                continue;
            }
            try {
                Field field = enumConstant.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Object fieldValue = field.get(enumConstant);
                if (fieldValue != null) {
                    values.add(fieldValue);
                }
            } catch (NoSuchFieldException | IllegalAccessException exception) {
                throw new IllegalArgumentException(
                        "Enum " + targetEnum.getName() + " has no accessible field '" + fieldName + "'",
                        exception
                );
            }
        }
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        for (Object enumValue : values) {
            if (enumValue.equals(value) || enumValue.toString().equals(value.toString())) {
                return true;
            }
        }
        return false;
    }
}
