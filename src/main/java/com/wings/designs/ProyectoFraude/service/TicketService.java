/*
 * Copyright (c) 2021. Wings Design.
 */

package com.wings.designs.ProyectoFraude.service;

import com.wings.designs.ProyectoFraude.persistence.model.Client;
import com.wings.designs.ProyectoFraude.persistence.model.Manager;
import com.wings.designs.ProyectoFraude.persistence.model.Ticket;
import com.wings.designs.ProyectoFraude.persistence.model.User;
import com.wings.designs.ProyectoFraude.persistence.repository.TicketRepository;
import com.wings.designs.ProyectoFraude.requestbody.NewTicketRequest;
import com.wings.designs.ProyectoFraude.service.notification.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * A service that provides all the methods need to retrieve
 * data and make changes on the ticket resource in the database.
 */
@Service
public class TicketService {
    /**
     * Class that has access to the query's on the ticket table
     * on the database.
     */
    private final TicketRepository ticketRepository;

    /**
     * Service with methods to make query's or consults on
     * the client table in the database.
     */
    private final ClientService clientService;

    /**
     * Service with methods to make query's or consults on
     * the user_account table in the database.
     */
    private final UserService userService;

    /**
     * Service with methods to make query's or consults on
     * the manager table in the database.
     */
    private final ManagerService managerService;

    /**
     * Service that allow to send emails.
     */
    private final NotificationService notificationService;

    /**
     * Main constructor.
     *
     * @param ticketRepository    Class that has access to the query's on the
     *                            ticket table on the database.
     * @param clientService       class with methods to make query's or consults
     *                            on the client table in the database.
     * @param userService         Service with methods to make query's or consults on
     *                            the user_account table in the database.
     * @param managerService      Service with methods to make query's or consults
     *                            on the manager table in the database.
     * @param notificationService Service that allow to send emails.
     */
    public TicketService(final TicketRepository ticketRepository,
                         final ClientService clientService,
                         final UserService userService,
                         final ManagerService managerService,
                         final NotificationService notificationService) {
        this.ticketRepository = ticketRepository;
        this.clientService = clientService;
        this.userService = userService;
        this.managerService = managerService;
        this.notificationService = notificationService;
    }

    /**
     * Get all the tickets on the database.
     *
     * @return A list with all the tickets on the database.
     * An empty list if there's not one.
     */
    public List<Ticket> getTickets() {
        return ticketRepository.findAll();
    }

    /**
     * Gives a list with all the tickets that a
     * manager can take.
     *
     * @return A list with all the tickets that can be
     * taken by a manager.
     */
    public List<Ticket> getTicketsAvailable() {
        return ticketRepository.findTicketByStatus(
                Ticket.enumStatesOfTicket.OPEN);
    }

    /**
     * Gives all the tickets that a manager have.
     *
     * @param managerRut the rut of the manager.
     * @return A list of the tickets that a manager have.
     * Empty list if he doesn't have any.
     */
    public List<Ticket> getTicketsByManager(final String managerRut) {
        return ticketRepository.findTicketByManagerRut(managerRut);
    }

    /**
     * Checks the information given to make a new ticket on
     * the database.
     *
     * @param ticketRequest the request with the information
     *                      about the ticket to be made.
     * @param userRut       the rut of the user that made the
     *                      ticket request.
     */
    public void addNewTicket(final NewTicketRequest ticketRequest,
                             final String userRut) {
        User user = userService.getUsersByRut(userRut);
        Client client = clientService.getClientByUser(user);
        Optional<Ticket> optionalTicket =
                ticketRepository.findTicketByCardTypeAndClientAndStatusNotLike(
                        ticketRequest.getCardType(),
                        Ticket.enumStatesOfTicket.CLOSED, client);
        if (optionalTicket.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "A ticket with that card is still processing");
        }
        Ticket ticket = new Ticket(ticketRequest.getCardType(),
                ticketRequest.getComment(),
                Ticket.enumStatesOfTicket.OPEN, client);
        ticketRepository.save(ticket);

    }

    /**
     * Gives a list of all the tickets that the manager has
     * taken under review, and are still not closed.
     *
     * @param userRut the rut of the manager.
     * @return A list with tickets. Or an empty list in case
     * the manager doesn't have any tickets under review.
     */
    public List<Ticket> getTicketsTakenByManager(final String userRut) {
        User user = userService.getUsersByRut(userRut);
        Manager manager = managerService.getManagerByUser(user);
        return this.ticketRepository.getTicketByClientAndStatusLike(
                Ticket.enumStatesOfTicket.PENDING, manager);
    }

    /**
     * Allow a manager to take a ticket with the id
     * given.
     *
     * @param userRut  the rut of the Manager tha wants to
     *                 take the ticket.
     * @param ticketId the id of the ticket that is
     *                 going to be taken.
     */
    @Transactional
    public void takeTicket(final String userRut, final Long ticketId) {
        User user = userService.getUsersByRut(userRut);
        Manager manager = managerService.getManagerByUser(user);
        Long numberOfTickets = ticketRepository.countByStatusAndManager(
                Ticket.enumStatesOfTicket.PENDING, manager);
        if (numberOfTickets > 6) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }
        Ticket ticket = ticketRepository.getTicketById(ticketId);
        if (ticket == null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                    "ticket not found");
        }
        if (ticket.getStatus() != Ticket.enumStatesOfTicket.OPEN) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                    "ticket is not open");
        }
        ticket.setStatus(Ticket.enumStatesOfTicket.PENDING);
        ticket.setManager(manager);
    }

    /**
     * Close a ticket with the given id if is under review
     * under the manager with the rut given.
     *
     * @param userRut  the rut of the manager that is under
     *                 control of the ticket.
     * @param ticketId the id of the ticket that
     *                 is going to be closed.
     */
    @Transactional
    public void closeTicket(final String userRut, final Long ticketId) {
        User user = userService.getUsersByRut(userRut);
        Manager manager = managerService.getManagerByUser(user);
        Ticket ticket = ticketRepository.getTicketById(ticketId);
        if (ticket == null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                    "ticket not found");
        }
        if (ticket.getStatus() != Ticket.enumStatesOfTicket.PENDING) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                    "ticket is not under review");
        }
        if (ticket.getManager() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                    "the ticket does not have any manager assigned");
        }
        if (!ticket.getManager().getId().equals(manager.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                    "the manager has not taken this ticket");
        }
        LocalDate localDate = LocalDate.now();
        ticket.setStatus(Ticket.enumStatesOfTicket.CLOSED);
        ticket.setEndDate(localDate);
        notificationService.sendNotificationToClient(ticket);
    }

    /**
     * Returns the ticket with the id given, if the ticket
     * doesn't exist gives a 404 http response.
     *
     * @param id the ticket looked.
     * @return the ticket with the id given.
     */
    public Ticket getTicket(final Long id) {
        if (!ticketRepository.findTicketById(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "the ticket doesn't exists");
        }
        return ticketRepository.getTicketById(id);
    }
}
