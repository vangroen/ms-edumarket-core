package com.ctcse.ms.edumarket.core.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@NotBlank(message = "{error.validation.notblank.with.field}")
public @interface NotBlankWithMessage {

    String fieldName();

    String message() default "{error.validation.notblank.with.field}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
