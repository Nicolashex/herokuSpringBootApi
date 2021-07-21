/*
 * Copyright (c) 2021. Wings Design.
 */

package com.wings.designs.ProyectoFraude.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wings.designs.ProyectoFraude.persistence.validation.ValidRut;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Client of the system with his attributes and
 * it's representation in the Database.
 * @author Nicolas Henriquez
 * @author Sebastian Zapata
 * @author Ignacio Cabrera
 * @author Maikol Leiva
 * @version 1.0
 */
@Entity(name = "client")
public class Client {
    /**
     * An identifier to difference a client to another in the same table.
     */
    @Id
    @SequenceGenerator(name = "client_sequence",
            sequenceName = "client_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "client_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    /**
     * Is an identifier credential, that it's unique for every chilean citizen.
     * For more information see the article on wikipedia about
     * <a href="https://es.wikipedia.org/wiki/Rol_%C3%9Anico_Tributario">
     *     RUT</a>.
     */
    @NotBlank
    @ValidRut
    @Column(name = "rut", updatable = false, nullable = false)
    private String rut;

    /**
     * Is the complete name of the Client.
     */
    @NotBlank
    @Pattern(message = "Not valid name: ${validatedValue}",
            regexp = "^[a-zA-Z]{4,}(?: [a-zA-Z]+)?(?: [a-zA-Z]+)?(?: [a-zA-Z]+)?$")
    @Column(name = "fullname", nullable = false)
    @JsonProperty("fullname")
    private String fullName;

    /**
     * Is the address of the Client.
     */
    @NotBlank
    @Column(name = "address", nullable = false, columnDefinition = "TEXT")
    private String address;

    /**
     * Is the email that the Client provides for contact.
     */
    @NotBlank
    @Email
    @Column(name = "email", updatable = false, unique = true)
    private String email;

    /**
     * It's the number for his Bank account.
     */
    @NotBlank
    @Pattern(message = "not a valid account number",
            regexp = "^[0-9]{8,12}$")
    @Column(name = "account_number", unique = true, updatable = false)
    private String account;

    /**
     * Is the phone number that the Client provides for contact.
     */
    @NotNull
    @NotBlank
    @Pattern(message = "not a valid phone number: ${validatedValue}",
             regexp = "^(\\+?56)?(\\s?)(0?9)(\\s?)[9876543]\\d{7}$")
    @Column(name = "phone_number", updatable = false, nullable = false)
    @JsonProperty("phone_number")
    private String phoneNumber;

    /**
     * Represent the {@link User user} associated to the client on the database.
     */
    @JsonIgnore
    @OneToOne(targetEntity = User.class, cascade = CascadeType.ALL)
    private User user;

    /**
     * It's a list of {@link Ticket tickets} that represents all the tickets
     * that the client has been made.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "client")
    private List<Ticket> ticketList;

    /**
     * An empty constructor.
     */
    public Client() {
        // Nothing here.
    }

    /**
     * Is the main constructor. Receives all the needed parameters
     * to put the client on the database.
     * @param rut It's the rut of the client.
     * @param fullName It's the complete name of the client.
     * @param address It's the address of the client.
     * @param email It's the email of the client.
     * @param account It's the account number of the client.
     * @param phoneNumber It's the phone number of the client.
     * @param user It's the user associated with the client on the system.
     */
    public Client(final String rut, final String fullName, final String address,
                  final String email, final String account,
                  final String phoneNumber, final User user) {
        this.rut = rut;
        this.fullName = fullName;
        this.address = address;
        this.email = email;
        this.account = account;
        this.phoneNumber = phoneNumber;
        this.user = user;
        this.ticketList = new ArrayList<>();
    }

    /**
     * Returns the id of client. The client must be on the database already,
     * otherwise it will not have an id.
     * @return the id of the client if it's a client on the database.
     * Otherwise returns null.
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the rut of the client.
     * @return rut of the client.
     */
    public String getRut() {
        return rut;
    }

    /**
     * Returns the name that the client provided.
     * @return the name of the client.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Returns the address that the client provided.
     * @return the address of the client.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns the contact email of the client.
     * @return the client email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the bank account number of the client.
     * @return the account number of the client.
     */
    public String getAccount() {
        return account;
    }

    /**
     * Returns the phone number of the client.
     * @return the phone number of the client.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Returns the user associated with the client in the system.
     * @return the {@link User user} associated with the client.
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns a list of all the tickets recorded in the database that
     * the client has made.
     * @return A List with all the tickets that the client has made. An empty
     * list if there's no ticket made by the client.
     */
    public List<Ticket> getTicketList() {
        return ticketList;
    }
}
