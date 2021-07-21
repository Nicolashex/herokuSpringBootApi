/*
 * Copyright (c) 2021. Wings Design.
 */
package com.wings.designs.ProyectoFraude.service;

import com.wings.designs.ProyectoFraude.persistence.model.User;
import com.wings.designs.ProyectoFraude.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serves as the layer that is behind the logic for the requests made in the
 * {@link UserRepository Repository layer}.
 */
@Service
public class UserService {
    /**
     * Class that allow to make changes or consults to
     * the table of users in the database.
     */
    private final UserRepository userRepository;

    /**
     * Main constructor.
     *
     * @param userRepository An object of the class
     *                       {@link UserRepository UserRepository}
     *                       that is needed to communicate with
     *                       the database.
     */
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Return a list of all users in the system.
     *
     * @return If there's users in the system return a list of
     * {@link User User}. If there's no users in the system, returns an empty
     * list
     */
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    /**
     * Look for user with a specified id, if found, returns it.
     *
     * @param id Is the Id of the {@link User User}
     * @return If the user exists return the {@link User User},
     * otherwise returns null.
     */
    public Optional<User> getUserById(final Long id) {
        return userRepository.findUsersById(id);
    }

    /**
     * Look if a user with given Rut exists by
     * providing an Optional object.
     *
     * @param rut the rut of the user looked.
     * @return An optional in any case.
     */
    public Optional<User> findUserByRut(final String rut) {
        return userRepository.findUsersByRut(rut);
    }

    /**
     * Retrieves a user that posses the given rut.
     *
     * @param rut rut of the user wanted.
     * @return the User with the rut, if there's
     * no User with that rut, returns null.
     */
    public User getUsersByRut(final String rut) {
        return userRepository.getUserByRut(rut);
    }

    /**
     * Take a instance of {@link User User} and before adding it to the system
     * look if other users has the same RUT or the same email, if that's the
     * case the user is not put in to the system. Otherwise, the user is added.
     *
     * @param user An instance of {@link User User}.
     * @throws IllegalStateException If the user to be added has a RUT that
     *                               other user has already taken in the system.
     * @throws IllegalStateException If the user to be added has an email that
     *                               other user has already in the system.
     */
    public void addNewUser(final User user) {
        Optional<User> usersOptional =
                userRepository.findUsersByRut(user.getRut());
        if (usersOptional.isPresent()) {
            throw new IllegalStateException("Rut taken");
        }
        userRepository.save(user);
    }

    /**
     * Given a rut return and Optional to check if a
     * user with that rut exists.
     *
     * @param rut rut of the user looked.
     * @return Optional object in any case.
     */
    public Optional<User> findUsersByRut(final String rut) {
        return userRepository.findUsersByRut(rut);
    }
}
