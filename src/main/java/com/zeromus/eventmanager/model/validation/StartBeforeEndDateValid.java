package com.zeromus.eventmanager.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = StartBeforeEndDateValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface StartBeforeEndDateValid {
    String message() default "La date de début doit être antérieure à la date de fin.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
