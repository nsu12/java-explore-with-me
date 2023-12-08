package ru.practicum.ewm.mainsvc.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotEarlyThanValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEarlyThan {
    int hours() default 0;
    String message() default "date must be not early than {hours} hours from now";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
