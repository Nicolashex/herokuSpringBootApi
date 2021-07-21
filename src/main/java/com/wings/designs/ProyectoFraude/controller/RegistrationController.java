/*
 * Copyright (c) 2021. Wings Design.
 */

package com.wings.designs.ProyectoFraude.controller;

import com.wings.designs.ProyectoFraude.requestbody.RegistrationRequest;
import com.wings.designs.ProyectoFraude.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * The rest Controller in charge of the Registration process on the API.
 *
 * @author Nicolas Henriquez
 * @author Sebastian Zapata
 * @author Ignacio Cabrera
 * @author Maikol Leiva
 * @version 1.0
 */
@RestController
@RequestMapping("/register")
public class RegistrationController {
    /**
     * service that allow to take data from
     * the client resource.
     */
    final ClientService clientService;

    /**
     * Main constructor.
     *
     * @param clientService service that allow to take data from
     *                      the client resource. It's autowired.
     */
    @Autowired
    public RegistrationController(final ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Register a new client on the API, with the data given on
     * the post Request.
     *
     * @param registrationRequest It's a {@link RegistrationRequest RegistrationRequest}
     *                            object that posses all the attributes necessaries
     *                            for a registration.
     */
    @PostMapping
    public void registerNewUser(@Valid @RequestBody final RegistrationRequest registrationRequest) {
        clientService.addNewClient(registrationRequest);

    }

}
