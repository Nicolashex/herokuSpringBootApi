/*
 * Copyright (c) 2021. Wings Design.
 */
package com.wings.designs.ProyectoFraude.persistence.model;

import com.wings.designs.ProyectoFraude.persistence.validation.ValidRut;

import javax.persistence.*;

/**
 * Represents a User of the system with his attributes and it's
 * representation in the Database.
 *
 * @author Nicolas Henriquez
 * @author Sebastian Zapata
 * @author Ignacio Cabrera
 * @author Maikol Leiva
 * @version 1.0
 */
@Entity(name = "user_account")
public class User {
    /**
     * It's the identifier of the user.
     */
    @Id
    @SequenceGenerator(name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "user_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    /**
     * Its the rut of the user. And identifier of every chilean citizen.
     */
    @ValidRut
    @Column(name = "rut", updatable = false, nullable = false)
    private String rut;

    /**
     * the password of the user. It's encrypted.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * The role assigned for the user in the system.
     */
    @ManyToOne()
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id",
            referencedColumnName = "id"), inverseJoinColumns =
    @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Role role;

    /**
     * An Empty Constructor.
     */
    public User() {
        // Nothing here.
    }

    /**
     * Constructor that receives all the attributes as parameters,
     * except for the id.
     *
     * @param rut      Represent the rut of the person. A unique number assigned
     *                 for every citizen that lives in Chile.
     * @param password Is the password of  the user, it has to be encrypted.
     * @param role     Is the type of rol that the user have.
     */
    public User(final String rut, final String password, final Role role) {
        this.rut = rut;
        this.password = password;
        this.role = role;
    }

    /**
     * Returns the id of user. The user must be on the database already,
     * otherwise it will not have an id.
     *
     * @return the id of the client if the client is on the database.
     * Otherwise returns null.
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the rut of the user.
     *
     * @return the rut of the user.
     */
    public String getRut() {
        return rut;
    }

    /**
     * Returns the password of the user.
     *
     * @return the password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the role of the user.
     *
     * @return the role of the user.
     */
    public Role getRole() {
        return role;
    }
}





