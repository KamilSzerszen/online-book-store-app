package org.example.bookstoreapp.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private String first;
    private String second;
    private String message;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.first = constraintAnnotation.first();
        this.second = constraintAnnotation.second();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {


        try {
            Class<?> clazz = o.getClass();
            Field password = clazz.getDeclaredField(first);
            Field repeatPassword = clazz.getDeclaredField(second);

            password.setAccessible(true);
            repeatPassword.setAccessible(true);

            Object first = password.get(o);
            Object second = repeatPassword.get(o);

            boolean valid = (first == null || second == null)

                    || (first != null && first.equals(second));

            if (!valid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(this.second)
                        .addConstraintViolation();
            }

            return valid;

        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }
}
