package ru.practicum.ewm.mainsvc.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class NotEarlyThanValidator implements ConstraintValidator<NotEarlyThan, LocalDateTime> {
    private int hoursBeforeNow;

    @Override
    public void initialize(NotEarlyThan annotation) {
        hoursBeforeNow = annotation.hours();
    }

    @Override
    public boolean isValid(LocalDateTime date, ConstraintValidatorContext ctx) {
        if (date == null) {
            return true;
        }

        return date.isAfter(LocalDateTime.now().plusHours(hoursBeforeNow));
    }
}
