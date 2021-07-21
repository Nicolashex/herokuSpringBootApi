/*
 * Copyright (c) 2021. Wings Design.
 */

package com.wings.designs.ProyectoFraude.service.notification;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration with information about the Notification
 * service provided through the application.properties file.
 */
@Configuration
@ConfigurationProperties(prefix = "spring.mail")
public class NotificationConfig {
    /**
     * the host for sending emails.
     */
    private String host;

    /**
     * the mail where it sends emails.
     */
    private String username;

    /**
     * the password of the username that sends emails.
     */
    private String password;

    /**
     * Empty Constructor.
     */
    public NotificationConfig() {
        // Nothing here.
    }

    /**
     * Gives the host.
     *
     * @return the host.
     */
    public String getHost() {
        return host;
    }

    /**
     * Gives the username.
     *
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gives the password.
     *
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the host with the given one.
     * It's used automatically to bind
     * with the value in the application.properties file.
     *
     * @param host host of the mail service.
     */
    public void setHost(final String host) {
        this.host = host;
    }

    /**
     * Set the username with the given one.
     * It's used automatically to bind
     * with the value in the application.properties file.
     *
     * @param username the new username to send emails.
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Set the password with the given one.
     * It's used automatically to bind
     * with the value in the application.properties file.
     *
     * @param password the new password of the username
     *                 that sends the emails.
     */
    public void setPassword(final String password) {
        this.password = password;
    }
}
