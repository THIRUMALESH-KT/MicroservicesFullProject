package com.leave.helper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import lombok.extern.slf4j.Slf4j;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = ValidFutureLocalDateValidator.class)
public @interface ValidFutureLocalDate {
    String message() default "Invalid LocalDate. Should be in the future and in 'yyyy-MM-dd' format.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
