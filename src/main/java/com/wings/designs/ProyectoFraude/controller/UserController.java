/*
 * Copyright (c) 2021. Wings Design.
 */
package com.wings.designs.ProyectoFraude.controller;

import com.wings.designs.ProyectoFraude.persistence.model.User;
import com.wings.designs.ProyectoFraude.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Serve as the layer that manages the routes involving the entity for the class
 * {@link User User} .
 *
 * @author Nicolas Henriquez
 * @author Sebastian Zapata
 * @author Ignacio Cabrera
 * @author Maikol Leiva
 * @version 1.0
 */
@RestController
@RequestMapping("users")
public class UserController {
    /**
     * service that allow to take data from
     * the user resource.
     */
    private final UserService userService;

    /**
     * Main constructor of the class.
     *
     * @param userService An object of the class
     *                    {@link UserService UserService} which contains
     *                    methods needed to implement the database requests.
     */
    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    /**
     * Give all the users that are stored in the database of the system
     * as list of instances of {@link User User}.
     * The number of instances is equal to the number of
     * users that the database of the system has.
     *
     * @return a list of {@link User User}. If there's no users, returns an
     * empty list.
     */
    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    /**
     * Receives a instance of an {@link User User} using the annotation
     * <Code>&#64;RequestBody</Code>.
     * Then proceeds to insert it in the system database.
     *
     * @param user An instance of the class {@link User User}. This object will
     *             come in a JSON content type.
     */
    @PostMapping("/create")
    public void registerNewUser(@RequestBody final User user) {
        userService.addNewUser(user);
    }
}
