package com.springbite.authorization_server.validation;

import com.springbite.authorization_server.validation.constraint.Unique;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@RequiredArgsConstructor
public class UniqueValidator implements ConstraintValidator<Unique, String> {

    private final Log logger = LogFactory.getLog(UniqueValidator.class);

    private static final String QUERY = "SELECT COUNT(*) FROM %s WHERE %s = :value";

    private final EntityManager entityManager;

    private String table;
    private String column;

    @Override
    public void initialize(Unique constraintAnnotation) {
        this.table = constraintAnnotation.table();
        this.column = constraintAnnotation.column();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        Long count = (Long) entityManager.createQuery(String.format(QUERY, table, column))
                .setParameter("value", value)
                .getSingleResult();

        return count == 0;
    }
}
