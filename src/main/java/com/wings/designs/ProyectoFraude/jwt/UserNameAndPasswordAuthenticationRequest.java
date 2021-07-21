/*
 * Copyright (c) 2021. Wings Design.
 */

package com.wings.designs.ProyectoFraude.jwt;

/**
 * Serves as the model for an authentication request.
 */
public class UserNameAndPasswordAuthenticationRequest {
    /**
     * The username given by the request.
     */
    private String username;
    /**
     * The password given by the request.
     */
    private String password;

    /**
     * Empty constructor.
     */
    public UserNameAndPasswordAuthenticationRequest() {
        // Nothing here.
    }

    /**
     * Retrieves the actual username.
     *
     * @return the username of the request
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username for the one given.
     *
     * @param username
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Get the actual password.
     *
     * @return the password of the request.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password for the one given.
     *
     * @param password the new password.
     */
    public void setPassword(final String password) {
        this.password = password;
    }
}
