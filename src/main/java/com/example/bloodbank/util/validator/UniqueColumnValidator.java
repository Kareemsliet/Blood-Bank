package com.example.bloodbank.util.validator;

import com.example.bloodbank.util.annotation.UniqueColumn;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Arrays;
import java.util.Map;

public class UniqueColumnValidator implements ConstraintValidator<UniqueColumn,Object> {

    @Autowired
    HttpServletRequest request;

    @Autowired
    EntityManager entityManager;

    Class<?> targetEntity;
    String columnName;
    String exceptColumn;
    String exceptPathVariable;
    String[] exceptPathMethods;

    public void initialize(UniqueColumn constraintAnnotation) {
        targetEntity = constraintAnnotation.targetEntity();
        columnName = constraintAnnotation.column();
        exceptColumn = constraintAnnotation.exceptColumn();
        exceptPathVariable = constraintAnnotation.exceptPathVariable();
        exceptPathMethods = constraintAnnotation.exceptPathMethods();
    }

    public boolean isValid(Object value,ConstraintValidatorContext constraintValidatorContext) {
        Object exceptValue = null;
        if(Arrays.asList(exceptPathMethods).contains(request.getMethod())){
            exceptValue = this.getExceptValue();
        }
        return sqlQuery(value, exceptValue) == 0;
    }

    private Object getExceptValue(){
        Object attribute = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (attribute instanceof Map<?, ?> requestAttributes && requestAttributes.containsKey(exceptPathVariable)) {
            Object value = requestAttributes.get(exceptPathVariable);
            return Long.valueOf(value.toString());
        }
        return null;
    }

    private long sqlQuery(Object value, Object exceptValue){
        String sqlRaw = "SELECT Count(*) FROM " + targetEntity.getName() + " t WHERE t." + columnName + " = :value";
        if(exceptValue != null){
            sqlRaw = sqlRaw + " AND t." + exceptColumn + " <> :exceptId";
        }
        TypedQuery<Long> query = this.entityManager.createQuery(sqlRaw,Long.class).setParameter("value",value);
        if(exceptValue != null){
            query = query.setParameter("exceptId",exceptValue);
        }
        return query.getSingleResult();
    }

}