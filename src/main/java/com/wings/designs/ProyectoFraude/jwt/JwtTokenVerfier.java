/*
 * Copyright (c) 2021. Wings Design.
 */

package com.wings.designs.ProyectoFraude.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class that allow to verify the token given by the client.
 */
public class JwtTokenVerfier extends OncePerRequestFilter {

    /**
     * The secret key used to validate the token.
     */
    private final SecretKey secretKey;

    /**
     * Configuration class that provides with information like
     * the expiration time  of the token or the prefix.
     */
    private final JwtConfig jwtConfig;

    /**
     * Main constructor.
     *
     * @param secretKey the secret key used on the token.
     * @param jwtConfig the configuration class with
     *                  information about the token.
     */
    public JwtTokenVerfier(final SecretKey secretKey,
                           final JwtConfig jwtConfig) {
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    /**
     * Simple implementation  of doFilterInternal that filters request
     * to check if there's any errors adding a layer to authenticate
     * if there's issues on the token provided.
     *
     * @param request     The request to process
     * @param response    The response associated with the request
     * @param filterChain Provides access to the next filter in the
     *                    chain for this filter to pass the request
     *                    and response to for further processing.
     * @throws IOException      if there's an error on the I/O on the request.
     * @throws ServletException if the processing on the request fails
     *                          for any other reason.
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader(
                jwtConfig.getAuthorizationHeader());
        if (authorizationHeader == null || authorizationHeader.isEmpty()
                || !authorizationHeader
                .startsWith(jwtConfig.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authorizationHeader.replace(
                jwtConfig.getTokenPrefix(), "");
        try {

            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build().parseClaimsJws(token);
            Claims body = claimsJws.getBody();
            String username = body.getSubject();
            List<Map<String, String>> authorities =
                    (List<Map<String, String>>) body.get("authorities");
            Set<SimpleGrantedAuthority> authority = authorities.stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                    .collect(Collectors.toSet());
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            username, null, authority);
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);


        } catch (JwtException e) {
            throw new AuthenticationException(e.getLocalizedMessage());
        }
        filterChain.doFilter(request, response);
    }
}
