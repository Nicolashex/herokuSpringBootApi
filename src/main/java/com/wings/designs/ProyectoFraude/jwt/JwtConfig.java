/*
 * Copyright (c) 2021. Wings Design.
 */

package com.wings.designs.ProyectoFraude.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

/**
 * A configuration class that allow to bind the information given into
 * the application.properties file related to jwt into the class attributes.
 */
@Configuration
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {
    /**
     * The secret key for the jason web token.
     */
    private String secretKey;

    /**
     * The prefix of the jason web token.
     */
    private String tokenPrefix;

    /**
     * the expiration in hours of the jason web token.
     */
    private Integer tokenExpirationAfterHours;

    /**
     * An empty constructor.
     */
    public JwtConfig() {
        //Nothing here.
    }

    /**
     * Returns the authorization header.
     *
     * @return the authorization header.
     */
    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }

    /**
     * Returns the secret key of the jwt.
     *
     * @return the secret key of the jwt.
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * Change the secret key with the given one.
     * It's used automatically when setting the value
     * from the application.properties file at the start.
     *
     * @param secretKey the new secret key.
     */
    public void setSecretKey(final String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * Returns the actual token prefix.
     *
     * @return the token prefix.
     */
    public String getTokenPrefix() {
        return tokenPrefix;
    }

    /**
     * Change the tokenPrefix with the given one.
     * It's used automatically when setting the value
     * from the application.properties file at the start.
     *
     * @param tokenPrefix the new token prefix.
     */
    public void setTokenPrefix(final String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    /**
     * Gives the expiration on hours of the token.
     *
     * @return the actual expiration in hours of the token.
     */
    public Integer getTokenExpirationAfterHours() {
        return tokenExpirationAfterHours;
    }

    /**
     * Change the expiration on hours of the token with the given value.
     * It's used automatically when setting the value
     * from the application.properties file at the start.
     *
     * @param tokenExpirationAfterHours the new time on hours for expiration
     *                                  of the token.
     */
    public void setTokenExpirationAfterHours(
            final Integer tokenExpirationAfterHours) {
        this.tokenExpirationAfterHours = tokenExpirationAfterHours;
    }
}
