/*
 * Copyright (c) 2021. Wings Design.
 */

package com.wings.designs.ProyectoFraude.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wings.designs.ProyectoFraude.persistence.model.Client;
import com.wings.designs.ProyectoFraude.persistence.model.Manager;
import com.wings.designs.ProyectoFraude.service.ClientService;
import com.wings.designs.ProyectoFraude.service.ManagerService;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Filters the username and password given when login.
 */
public class JwtUsernameAndPasswordAuthenticationFilter
        extends UsernamePasswordAuthenticationFilter {

    /**
     * Allow to process the method needed to authenticate
     * password and username given.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Gives all information about the JWT token.
     */
    private final JwtConfig jwtConfig;

    /**
     * Gives the secret key for the JWT token.
     */
    private final SecretKey secretKey;

    /**
     * Necessary to give information about the user if
     * it's a manager.
     */
    private final ManagerService managerService;

    /**
     * Necessary to give information about the user if
     * it's a client.
     */
    private final ClientService clientService;

    /**
     * Main constructor.
     *
     * @param authenticationManager class that authenticate password
     *                              and username given.
     * @param jwtConfig             information about the JWT token.
     * @param secretKey             the secret key for the JWT token.
     */
    public JwtUsernameAndPasswordAuthenticationFilter(
            final AuthenticationManager authenticationManager,
            final JwtConfig jwtConfig,
            final SecretKey secretKey,
            final ManagerService managerService,
            final ClientService clientService) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
        this.managerService = managerService;
        this.clientService = clientService;
    }

    /**
     * Authenticates the  request given. And gives an authenticated object.
     *
     * @param request  the http request given.
     * @param response response of the request.
     * @return a fully authenticated object including credentials.
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request,
                                                final HttpServletResponse response)
            throws AuthenticationException {
        try {
            UserNameAndPasswordAuthenticationRequest authenticationRequest =
                    new ObjectMapper().readValue(request.getInputStream(),
                            UserNameAndPasswordAuthenticationRequest.class);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    );

            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * After a successful authentication, response to
     * the client with the token created.
     *
     * @param request    request received.
     * @param response   response object that is going to be send.
     * @param chain      object used to process another filter.
     * @param authResult class that provides all the information
     *                   of the user that passed the
     *                   authentication filter.
     * @throws IOException      For an input error when management of the data.
     * @throws ServletException In any other error.
     */
    @Override
    protected void successfulAuthentication(final HttpServletRequest request,
                                            final HttpServletResponse response,
                                            final FilterChain chain,
                                            final Authentication authResult)
            throws IOException, ServletException {
        String name = "";
        Long id = null;
        if (authResult.getAuthorities().toString().equals("[ROLE_CLIENT]")) {
            Client client = this.clientService
                    .getClientByRut(authResult.getName());
            name = client.getFullName();
            id = client.getId();
        }
        if (authResult.getAuthorities().toString().equals("[ROLE_MANAGER]")) {
            Manager manager = this.managerService
                    .getManagerByRut(authResult.getName());
            name = manager.getFullName();
            id = manager.getId();
        }
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.setTime(new Date());
        expirationDate.add(Calendar.HOUR_OF_DAY,
                jwtConfig.getTokenExpirationAfterHours());
        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("name", name)
                .claim("id", id)
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(expirationDate.getTime())
                .signWith(secretKey)
                .compact();

        response.addHeader(jwtConfig.getAuthorizationHeader(),
                jwtConfig.getTokenPrefix() + token);
    }
}
