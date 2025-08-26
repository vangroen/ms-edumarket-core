package com.ctcse.ms.edumarket.core.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Size
public @interface SizeWithMessage {

    String fieldName();

    int min() default 0;
    int max() default Integer.MAX_VALUE;

    String message() default "{error.validation.size.with.field}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
