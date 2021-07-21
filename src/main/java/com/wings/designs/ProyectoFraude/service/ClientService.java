/*
 * Copyright (c) 2021. Wings Design.
 */

package com.wings.designs.ProyectoFraude.service;

import com.wings.designs.ProyectoFraude.persistence.model.Client;
import com.wings.designs.ProyectoFraude.persistence.model.Role;
import com.wings.designs.ProyectoFraude.persistence.model.User;
import com.wings.designs.ProyectoFraude.persistence.repository.ClientRepository;
import com.wings.designs.ProyectoFraude.requestbody.RegistrationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    /**
     * Used to make request or changes into the
     * database client table through the methods defined in this
     * service.
     */
    private final ClientRepository clientRepository;

    /**
     * Used to make requests into the role table in
     * the database through methods defined in this
     * service.
     */
    private final RoleService roleService;

    /**
     * Used to encrypt the password when a user
     * make a register request.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Used to make request or changes into the
     * database user table through the methods defined in this
     * service.
     */
    private final UserService userService;

    /**
     *
     * @param clientRepository repository to make changes request or changes
     *                         into the database client table
     * @param roleService service to make changes request into the role table in
     *                    the database
     * @param passwordEncoder Encoder used to encrypt a password.
     * @param userService service to make request or changes into the
     *                    database user table.
     */
    public ClientService(final ClientRepository clientRepository,
                         final RoleService roleService,
                         final PasswordEncoder passwordEncoder,
                         final UserService userService) {
        this.clientRepository = clientRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    /**
     * Returns all the clients on the database.
     * @return A List with all the clients.
     * Empty list if there's no clients.
     */
    public List<Client> getClients() {
        return clientRepository.findAll();
    }

    /**
     * Returns the client with the given rut.
     * @param rut rut of the client wanted.
     * @return A client if found, or a null in
     * the other case.
     */
    public Client getClientByRut(final String rut) {
        return clientRepository.getClientByRut(rut);
    }

    /**
     * Returns a client with the given user.
     * @param user the user of the client wanted.
     * @return A client if found, or a null in
     * the other case.
     */
    public Client getClientByUser(final User user) {
        return clientRepository.getClientByUser(user);
    }

    /**
     * Register a new client with the data given through
     * the registration request.
     * @param registrationRequest the request with all the
     *                            information needed to register
     *                            a client into the system.
     * @throws ConstraintViolationException If the data given
     * is not valid.
     */
    @Transactional
    public void addNewClient(final RegistrationRequest registrationRequest)
            throws ConstraintViolationException {

        Optional<User> clientOptional =
                userService.findUserByRut(registrationRequest.getRut());
        Optional<Client> clientOptional2 =
                clientRepository.findClientsByAccount(
                        registrationRequest.getAccount());
        Optional<Client> clientOptional3 = clientRepository.findClientsByEmail(
                registrationRequest.getEmail());
        if (clientOptional.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Rut already taken");
        }
        if (clientOptional2.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "account number already taken");
        }
        if (clientOptional3.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, "email taken");
        }
        User userForNewClient = new User(registrationRequest.getRut(),
                passwordEncoder.encode(registrationRequest.getPassword()),
                roleService.findRoleByName(Role.enumRole.ROLE_CLIENT));
        Client newClient = new Client(registrationRequest.getRut(),
                registrationRequest.getFullName(),
                registrationRequest.getAddress(),
                registrationRequest.getEmail(),
                registrationRequest.getAccount(),
                registrationRequest.getPhoneNumber(),
                userForNewClient);
        clientRepository.save(newClient);
    }
}
