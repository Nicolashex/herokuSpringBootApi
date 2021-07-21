/*
 * Copyright (c) 2021. Wings Design.
 */

package com.wings.designs.ProyectoFraude.security;

import com.wings.designs.ProyectoFraude.jwt.JwtConfig;
import com.wings.designs.ProyectoFraude.jwt.JwtTokenVerfier;
import com.wings.designs.ProyectoFraude.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.wings.designs.ProyectoFraude.service.ClientService;
import com.wings.designs.ProyectoFraude.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;
import java.util.Arrays;


@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * The secret key used on the JWT token.
     */
    private final SecretKey secretKey;

    /**
     * Configuration class with information about
     * the JWT token.
     */
    private final JwtConfig jwtConfig;

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
     * Main Constructor.
     * @param secretKey secret key used on the JWT token.
     * @param jwtConfig class with information about
     *                  the JWT token.
     */
    @Autowired
    public ApplicationSecurityConfig(final SecretKey secretKey,
                                     final JwtConfig jwtConfig,
                                     final ClientService clientService,
                                     final ManagerService managerService) {
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
        this.managerService = managerService;
        this.clientService = clientService;
    }

    /**
     * Set the security configuration for the HTTP
     * security.
     * @param http the HttpSecurity to be modified.
     * @throws Exception
     */
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(
                        authenticationManager(),
                        jwtConfig,
                        secretKey,
                        managerService,
                        clientService))
                .addFilterAfter(new JwtTokenVerfier(secretKey, jwtConfig),
                        JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/register/**").permitAll()
                .antMatchers(HttpMethod.GET, "/managers/**")
                .hasRole("MANAGER")
                .antMatchers(HttpMethod.GET, "/tickets/**")
                .hasRole("MANAGER")
                .antMatchers(HttpMethod.PATCH, "/tickets/**")
                .hasRole("MANAGER")
                .antMatchers(HttpMethod.POST, "/tickets")
                .hasRole("CLIENT")
                .anyRequest()
                .authenticated();

    }

    /**
     * Change the Cors configuration with the
     * one set on this method.
     * @return Class with the new Configuration for
     * Cors policy.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        source.registerCorsConfiguration(
                "/**", config.applyPermitDefaultValues());
        //allow Authorization to be exposed
        config.setExposedHeaders(Arrays.asList("Authorization"));
        return source;
    }

}
