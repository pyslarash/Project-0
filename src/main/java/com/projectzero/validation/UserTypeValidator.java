package com.projectzero.validation;

import com.projectzero.enums.UserType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserTypeValidator implements ConstraintValidator<ValidUserType, String> {

    @Override
    public void initialize(ValidUserType constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            UserType.fromString(value); // Try to convert the string to UserType enum
            return true; // If successful, the value is valid
        } catch (IllegalArgumentException e) {
            return false; // If an exception is thrown, the value is not valid
        }
    }
}
