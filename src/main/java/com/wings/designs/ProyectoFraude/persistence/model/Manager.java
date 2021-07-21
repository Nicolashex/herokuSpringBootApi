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
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Manager of the system with his attributes
 * and it's representation in the Database. *
 *
 * @author Nicolas Henriquez
 * @author Sebastian Zapata
 * @author Ignacio Cabrera
 * @author Maikol Leiva
 * @version 1.0
 */
@Entity(name = "manager")
public class Manager {
    /**
     * An identifier to difference a manager to another in the same table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "manager_secuence")
    @SequenceGenerator(name = "manager_secuence", sequenceName = "manager_secuence",
            allocationSize = 1, initialValue = 1)
    @Column(name = "id", updatable = false)
    private Long id;

    /**
     * Is an identifier credential, that it's unique for every chilean citizen.
     * For more information see the article on wikipedia about
     * <a href="https://es.wikipedia.org/wiki/Rol_%C3%9Anico_Tributario">
     * RUT</a>.
     */
    @NotBlank
    @ValidRut
    @Column(name = "rut", updatable = false, nullable = false)
    private String rut;

    /**
     * It's the complete name of the manager.
     */
    @NotBlank
    @Pattern(message = "Not valid name: ${validatedValue}",
            regexp = "^[a-zA-Z]{4,}(?: [a-zA-Z]+)?(?: [a-zA-Z]+)?(?: [a-zA-Z]+)?$")
    @Column(name = "fullname", nullable = false)
    @JsonProperty("fullname")
    private String fullName;

    /**
     * It's the contact email of the manager.
     */
    @Email
    @Column(name = "email", updatable = false, unique = true)
    private String email;

    /**
     * It's the address of the manager.
     */
    @Column(name = "address", nullable = false, columnDefinition = "TEXT")
    private String address;

    /**
     * Represent the {@link User user} associated to the client on the database.
     */
    @JsonIgnore
    @OneToOne(targetEntity = User.class, cascade = CascadeType.ALL)
    private User user;

    /**
     * It's a list of {@link Ticket tickets} that represents all the tickets
     * that the manager has taken.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "manager")
    private List<Ticket> ticketList;

    /**
     * An empty constructor.
     */
    public Manager() {
        // Nothing here.
    }

    /**
     * Is the main constructor. Receives all the needed parameters
     * to put the manager on the database.
     *
     * @param rut      It's the rut of the manager.
     * @param fullName It's the complete name of the manager.
     * @param email    It's the contact email of the manager.
     * @param address  It's the address of the manager.
     * @param user     It's the user associated with the manager.
     */
    public Manager(final String rut, final String fullName,
                   final String email, final String address,
                   final User user) {
        this.rut = rut;
        this.fullName = fullName;
        this.email = email;
        this.address = address;
        this.user = user;
        this.ticketList = new ArrayList<>();
    }

    /**
     * Returns the id of client. The client must be on the database already,
     * otherwise it will not have an id.
     *
     * @return the id of the manager if the manager is already on the database.
     * Otherwise returns null.
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the rut of the manager.
     *
     * @return the rut of the manager.
     */
    public String getRut() {
        return rut;
    }

    /**
     * Returns the complete name of the manager.
     *
     * @return the complete name of the manager.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Returns the contact email of the manager.
     *
     * @return the email of the manager.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the address of the manager.
     *
     * @return the address of the manager.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns the user associated with the manager in the system.
     *
     * @return the {@link User user} associated with the manager.
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns a list of all the tickets recorded in the database that
     * the manager has taken.
     *
     * @return A List with all the tickets that the manager has taken.
     * An empty list if there's no ticket taken by the manager.
     */
    public List<Ticket> getTicketList() {
        return ticketList;
    }

    /**
     * Receive a ticket that the manager want's to take, and add
     * it to his list of tickets.
     *
     * @param ticket It's the ticket that the manager want's to take.
     */
    public void addNewTicket(final Ticket ticket) {
        this.ticketList.add(ticket);
    }
}
