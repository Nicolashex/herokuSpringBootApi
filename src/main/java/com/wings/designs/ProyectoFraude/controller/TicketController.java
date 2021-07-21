/*
 * Copyright (c) 2021. Wings Design.
 */
package com.wings.designs.ProyectoFraude.controller;

import com.wings.designs.ProyectoFraude.persistence.model.Ticket;
import com.wings.designs.ProyectoFraude.requestbody.NewTicketRequest;
import com.wings.designs.ProyectoFraude.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * The rest Controller in charge of handling the
 * {@link Ticket ticket} resource on the API.
 *
 * @author Nicolas Henriquez
 * @author Sebastian Zapata
 * @author Ignacio Cabrera
 * @author Maikol Leiva
 * @version 1.0
 */
@RestController
@RequestMapping("/tickets")
public class TicketController {

    /**
     * service that allow to take data from
     * the ticket resource.
     */
    private final TicketService ticketService;

    /**
     * Main constructor.
     *
     * @param ticketService service that allow to take data from
     *                      the ticket resource. It's autowired.
     */
    @Autowired
    public TicketController(final TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Give all the tickets that are present on the API database.
     *
     * @return A list of all the tickets on the API, if there's no tickets
     * then give an empty list.
     */
    @GetMapping
    public List<Ticket> getTickets() {
        return ticketService.getTickets();
    }

    /**
     * Return the ticket with the given id.
     *
     * @param id Id of the ticket looked.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/")
    public Ticket getTicket(@PathVariable final Long id) {
        return ticketService.getTicket(id);
    }

    /**
     * Give all the tickets that are still with an Open Status.
     *
     * @return A list with all the Tickets that are available to
     * be taken by the managers. If there's no ticket with that status, then gives
     * an empty list
     */
    @GetMapping("/open/")
    public List<Ticket> getTicketsAvailable() {
        return ticketService.getTicketsAvailable();
    }

    /**
     * Give all the ticket that are still under review by a manager.
     *
     * @return A list with all the Tickets that are under review
     * by the managers. If there's no ticket with that status, then gives
     * an empty list
     */
    @GetMapping("/pending/")
    public List<Ticket> getTicketsTaken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userRut = (String) auth.getPrincipal();
        return ticketService.getTicketsTakenByManager(userRut);
    }

    /**
     * Create a ticket with the given information in the Request.
     *
     * @param request An object that has all the attributes needed
     *                to create a new ticket on the system.
     */
    @PostMapping()
    public void registerNewTicket(final @RequestBody NewTicketRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userRut = (String) auth.getPrincipal();
        ticketService.addNewTicket(request, userRut);
    }

    /**
     * Given a ticket id, if the status given is open then
     * the manager takes under review the ticket. If the status
     * is pending then the ticket is closed.
     *
     * @param id It's the id of the ticket that the manager
     *           wants to take under review or close.
     */
    @RequestMapping(method = RequestMethod.POST, value = "/{id}/")
    public void changeTicketStatus(@PathVariable final Long id,
                                   @RequestParam final String status) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userRut = (String) auth.getPrincipal();
        System.out.println(status);
        if (status.equals("open")) {
            ticketService.takeTicket(userRut, id);
        } else {
            if (status.equals("pending")) {
                ticketService.closeTicket(userRut, id);
            } else {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
    }
}
