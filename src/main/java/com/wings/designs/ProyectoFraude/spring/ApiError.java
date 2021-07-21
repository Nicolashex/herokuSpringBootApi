/*
 * Copyright (c) 2021. Wings Design.
 */

package com.wings.designs.ProyectoFraude.spring;

import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

/**
 * Serves as the body to make responses when a Exception is handle.
 */
public class ApiError {

    /**
     * Http status sent.
     */
    private final HttpStatus status;

    /**
     * message send in the response.
     */
    private final String message;

    /**
     * list of errors in the exception caught.
     */
    private final List<String> errors;

    /**
     * Constructor used when there are multiple
     * errors caught in the exception.
     * @param status status used to be sent.
     * @param message message used to be sent.
     * @param errors errors list to be sent.
     */
    public ApiError(final HttpStatus status, final String message,
                    final List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    /**
     * Constructor used when there's only one error
     * caught in the exception.
     * @param status status used to be sent.
     * @param message message used to be sent.
     * @param error error to be sent.
     */
    public ApiError(final HttpStatus status, final String message,
                    final String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }

    /**
     * Returns the status.
     * @return the actual status
     */
    public HttpStatus getStatus() {
        return this.status;
    }

    /**
     * Returns the message.
     * @return the current message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the error.
     * @return the current error.
     */
    public List<String> getErrors() {
        return errors;
    }
}
