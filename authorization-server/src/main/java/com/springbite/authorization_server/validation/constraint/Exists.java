package com.springbite.authorization_server.validation.constraint;

import com.springbite.authorization_server.validation.ExistsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * The annotated element must be existed in table-specific column
 * <p>
 * {@code null} elements are considered valid.
 * </p>
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistsValidator.class)
public @interface Exists {

    String message() default "Element does not exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<?> table();

    String column();
}
