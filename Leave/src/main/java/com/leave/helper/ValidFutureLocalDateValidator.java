package com.leave.helper;

import java.time.LocalDate;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;




@Slf4j
public class ValidFutureLocalDateValidator implements ConstraintValidator<ValidFutureLocalDate, LocalDate> {
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        log.info("*********inside Date Validation");
        LocalDate currentDate = LocalDate.now();
        
        if (value == null) {
            return true; // Valid because it's not mandatory
        }
        
        if (value.isAfter(currentDate)) {
            return true; // Valid future date
        } else {
            context.disableDefaultConstraintViolation(); // Disable the default error message

            if (!isValidDateFormat(value)) {
                context
                    .buildConstraintViolationWithTemplate("Invalid Date Format")
                    .addConstraintViolation();
            } else {
                context
                    .buildConstraintViolationWithTemplate("Invalid Date")
                    .addConstraintViolation();
            }
            
            return false; // Return false for invalid dates
        }
    }

    private boolean isValidDateFormat(LocalDate date) {
        try {
            LocalDate.parse(date.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}






	

