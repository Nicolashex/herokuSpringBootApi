/*
 * Copyright (c) 2021. Wings Design.
 */

package com.wings.designs.ProyectoFraude.controller;

import com.wings.designs.ProyectoFraude.persistence.model.Client;
import com.wings.designs.ProyectoFraude.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The rest Controller in charge of the {@link Client client}
 * resource's endpoints on the API.
 *
 * @author Nicolas Henriquez
 * @author Sebastian Zapata
 * @author Ignacio Cabrera
 * @author Maikol Leiva
 * @version 1.0
 */
@RestController
@RequestMapping("/clients")
public class ClientController {
    /**
     * service that allow to take data from
     * the client resource.
     */
    private final ClientService clientService;

    /**
     * Main constructor..
     *
     * @param clientService service that allow to take data from
     *                      the client resource. It's autowired.
     */
    @Autowired
    public ClientController(final ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Get all the clients from the database.
     *
     * @return A list of clients, if there's clients on the database.
     * Or an empty list in case there's no clients.
     */
    @GetMapping
    public List<Client> getClients() {
        return clientService.getClients();
    }


}
