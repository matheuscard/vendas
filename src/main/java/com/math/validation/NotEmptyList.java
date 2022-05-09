package com.math.validation;

import com.math.validation.constraintvalidation.NotEmptyListValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy= NotEmptyListValidator.class)
public @interface NotEmptyList {
    String message() default "A lista não pode estar vazia.";
    //Estes dois métodos abaixo são obrigatórios em uma anotação de validação.
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
