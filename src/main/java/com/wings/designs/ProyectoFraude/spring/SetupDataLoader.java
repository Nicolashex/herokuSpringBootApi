/*
 * Copyright (c) 2021. Wings Design.
 */

package com.wings.designs.ProyectoFraude.spring;

import com.wings.designs.ProyectoFraude.persistence.model.Manager;
import com.wings.designs.ProyectoFraude.persistence.model.User;
import com.wings.designs.ProyectoFraude.persistence.model.Role;
import com.wings.designs.ProyectoFraude.service.ManagerService;
import com.wings.designs.ProyectoFraude.service.RoleService;
import com.wings.designs.ProyectoFraude.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.*;

@Component
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {
    /**
     * Flag to check if the method to
     * set things at the start was executed already.
     */
    private boolean alreadySetup = false;

    /**
     * Service to retrieve and send information
     * to the user table in the database.
     */
    @Autowired
    private UserService userService;

    /**
     * Service to retrieve and send information
     * to the manager table in the database.
     */
    @Autowired
    private ManagerService managerService;

    /**
     * Class that though his methods allow
     * to make request or retrieve data to
     * the role table in the database.
     */
    @Autowired
    private RoleService roleService;

    /**
     * Encoder of passwords.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * Set the database with the roles of the users,
     * also insert all the managers in the database.
     * @param event the event to respond to.
     */
    @Override
    @TransactionalEventListener
    public void onApplicationEvent(final ContextRefreshedEvent event) {

        if (alreadySetup) {
            return;
        }
        createRole(Role.enumRole.ROLE_MANAGER);
        createRole(Role.enumRole.ROLE_CLIENT);
        Role managerRole = roleService.findRoleByName(
                Role.enumRole.ROLE_MANAGER);
        createManagerUsers("10744718-0", "correofalso@gmail.com",
                "Marco Polo", "Calle falsa 123",
                managerRole, "1234");
        createManagerUsers("13933875-8", "correo123@gmail.com",
                "Juan Carlos", "Calle falsa 2234",
                managerRole, "1234");
        alreadySetup = true;

    }

    /**
     * Receives the name of the Role, and if the name is
     * not found in the Role table, then add a Role to the table with that
     * name and privileges altogether.
     *
     * @param name it's the name of the Role.
     */
    @Transactional
    void createRole(final Role.enumRole name) {
        Role role = roleService.findRoleByName(name);
        if (role == null) {
            role = new Role(name);
        }
        role = roleService.save(role);
    }

    /**
     * Given all the information needed to create a manager,
     * creates one.
     * @param rut rut of the manager.
     * @param email email of the manager.
     * @param fullname complete name of the manager.
     * @param address address of the manager.
     * @param role role of the manager.
     * @param password password of the manager.
     */
    @Transactional
    void createManagerUsers(final String rut, final String email,
                            final String fullname, final String address,
                            final Role role, final String password) {
        Optional<User> user = userService.findUserByRut(rut);
        Optional<Manager> manager = managerService.findManagerByEmail(email);
        if (!user.isPresent() && !manager.isPresent()) {
            User newUser = new User(rut, passwordEncoder.encode(password), role);
            Manager newManager = new Manager(rut, fullname, email,
                    address, newUser);
            this.managerService.addNewManager(newManager);
        }
    }
}
