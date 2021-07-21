/*
 * Copyright (c) 2021. Wings Design.
 */

package com.wings.designs.ProyectoFraude.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * Class that manages the secret key of the JWT.
 */
@Configuration
public class JwtSecretKey {

    /**
     * Provides the secret key given on the application.properties file.
     */
    private final JwtConfig jwtConfig;

    /**
     * Main constructor. Autowired.
     *
     * @param jwtConfig the configuration class that has the raw secret key.
     */
    @Autowired
    public JwtSecretKey(final JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    /**
     * Returns the secret key.
     *
     * @return the secret key.
     */
    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(
                jwtConfig.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }
}
