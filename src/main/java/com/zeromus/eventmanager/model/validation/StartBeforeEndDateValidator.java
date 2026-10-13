package com.zeromus.eventmanager.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StartBeforeEndDateValidator implements ConstraintValidator<StartBeforeEndDateValid, StartEndDateable> {

    @Override
    public boolean isValid(StartEndDateable bean, ConstraintValidatorContext context) {
        if (bean == null || bean.getStartDate() == null || bean.getEndDate() == null) {
            return true;
        }

        return !bean.getStartDate().isAfter(bean.getEndDate());
    }
}