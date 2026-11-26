package com.example.bloodbank.util.validator;

import com.example.bloodbank.util.annotation.ExistsColumn;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ExistsColumnValidator implements ConstraintValidator<ExistsColumn, Object> {

    @Autowired
    EntityManager entityManager;

    Class<?> targetEntity;
    String columnName;

    public void initialize(ExistsColumn constraintAnnotation) {
        targetEntity = constraintAnnotation.targetEntity();
        columnName = constraintAnnotation.column();
    }

    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        return sqlQuery(value) == 1;
    }

    private long sqlQuery(Object value){
        String sqlRaw = "SELECT Count(*) FROM " + targetEntity.getName() + " t WHERE t." + columnName + " = :value";
        TypedQuery<Long> query = this.entityManager.createQuery(sqlRaw,Long.class).setParameter("value",value);
        return query.getSingleResult();
    }

}
