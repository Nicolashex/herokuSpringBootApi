/*
 * Copyright (c) 2021. Wings Design.
 */

package com.wings.designs.ProyectoFraude.requestbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wings.designs.ProyectoFraude.persistence.validation.ValidRut;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class RegistrationRequest {
    /**
     * The rut of the user that is going to be registered.
     */
    @NotBlank
    @ValidRut
    private final String rut;

    /**
     * the password of the client registered.
     */
    @NotBlank
    @Pattern(message = "not a valid account number",
            regexp = "^[0-9]{4}$")
    private final String password;

    /**
     * The full name of the client registered.
     */
    @NotBlank
    @Pattern(message = "Not valid name: ${validatedValue}",
            regexp = "^[a-zA-Z]{4,}(?: [a-zA-Z]+)?(?: [a-zA-Z]+)?(?: [a-zA-Z]+)?$")
    @JsonProperty("fullname")
    private final String fullName;

    /**
     * the address of the client registered.
     */
    @NotBlank
    private final String address;

    /**
     * the email of the client registered.
     */
    @NotBlank
    @Email
    private final String email;

    /**
     * the account number of the client registered.
     */
    @NotBlank
    @Pattern(message = "not a valid account number",
            regexp = "^[0-9]{8,12}$")
    private final String account;

    /**
     * the phone number of the client registered.
     */
    @NotBlank
    @Pattern(message = "not a valid phone number: ${validatedValue}",
            regexp = "^(\\+?56)?(\\s?)(0?9)(\\s?)[9876543]\\d{7}$")
    @JsonProperty("phone_number")
    private final String phoneNumber;

    /**
     * Main constructor.
     *
     * @param rut         of the client.
     * @param password    password of the client.
     * @param fullName    complete name of the client.
     * @param address     address of the client.
     * @param email       email of the client.
     * @param account     account of the client.
     * @param phoneNumber phone number of the client.
     */
    public RegistrationRequest(final String rut, final String password,
                               final String fullName, final String address,
                               final String email, final String account,
                               final String phoneNumber) {
        this.rut = rut;
        this.password = password;
        this.fullName = fullName;
        this.address = address;
        this.email = email;
        this.account = account;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gives the rut that the registration request has.
     *
     * @return the rut of the Request for registration.
     */
    public String getRut() {
        return rut;
    }

    /**
     * Gives the password that the registration request has.
     *
     * @return the password of the request.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gives the complete name that the registration request has.
     *
     * @return the full name of the request.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Gives the address that is put on the registration request.
     *
     * @return the address of the request.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gives the email that is put on the registration request.
     *
     * @return the email of the request.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gives the account number that is put on the registration request.
     *
     * @return the account number of the request.
     */
    public String getAccount() {
        return account;
    }

    /**
     * Gives the phone number that the client put on the registration request.
     *
     * @return the phone number of the request.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
}
