/*
 * Copyright (c) 2021. Wings Design.
 */

package com.wings.designs.ProyectoFraude.persistence.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Class that validates the rut. It's used with
 * Bean validation.
 */
public class ValidRutValidator implements
        ConstraintValidator<ValidRut, String> {
    /**
     * Initializes the validator.
     *
     * @param constraint the constraint applied for the validation.
     */
    public void initialize(final ValidRut constraint) {
    }

    /**
     * Given a rut, checks if it's valid or not and
     * send a boolean as a result.
     *
     * @param rut     The rut to ve validate
     * @param context Gives extra information of
     *                the context of the validation.
     * @return true if the rut is valid. False in the other case.
     */
    public boolean isValid(String rut,
                           final ConstraintValidatorContext context) {
        boolean validation = false;
        try {
            rut = rut.toUpperCase();
            rut = rut.replace("-", "");
            int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));
            char dv = rut.charAt(rut.length() - 1);
            int m = 0;
            int s = 1;
            for (; rutAux != 0; rutAux /= 10) {
                s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
            }
            if (dv == (char) (s != 0 ? s + 47 : 75)) {
                validation = true;
            }

        } catch (java.lang.NumberFormatException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
        return validation;
    }


}
