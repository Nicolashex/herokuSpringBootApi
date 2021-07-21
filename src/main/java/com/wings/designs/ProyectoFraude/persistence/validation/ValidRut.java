/*
 * Copyright (c) 2021. Wings Design.
 */

package com.wings.designs.ProyectoFraude.persistence.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;

/**
 * Validation interface used to valid Rut.
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidRutValidator.class)
public @interface ValidRut {
    /**
     * Gives the default message when the validation is not passed.
     *
     * @return the default message when the validation is not passed.
     */
    String message() default "Rut is not valid";

    Class<?>[] groups() default {};

    /**
     * Return an array of payloads.
     *
     * @return An array with Payloads object that transfer
     * metadata.
     */
    Class<? extends Payload>[] payload() default {};
}
