/*
 * Copyright (c) 2021. Wings Design.
 */
package com.wings.designs.ProyectoFraude.persistence.repository;

import com.wings.designs.ProyectoFraude.persistence.model.Client;
import com.wings.designs.ProyectoFraude.persistence.model.Manager;
import com.wings.designs.ProyectoFraude.persistence.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Layer that manages the requests made for the database
 * that are related to the entity User.
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    /**
     * Search for the ticket's that have a determined status,
     * and return all the matches.
     *
     * @param status is the status of the tickets wanted.
     * @return A list of all the {@link Ticket Tickets} with the status defined.
     * If there's no tickets with that status, returns an empty list.
     */
    @Query("FROM Ticket  WHERE status=?1")
    List<Ticket> findTicketByStatus(Ticket.enumStatesOfTicket status);

    /**
     * Search for all the tickets that have the specified rut as
     * it's manager rut. Returns all matches.
     *
     * @param rut Is the rut of the manager that is responsible for the ticket.
     * @return A list of all the {@link Ticket Tickets} that are managed by
     * the manager with the rut defined.If there's no tickets,
     * returns an empty list.
     */
    @Query("FROM Ticket  WHERE manager=?1 order by id")
    List<Ticket> findTicketByManagerRut(String rut);

    /**
     * Search for a Ticket with the card type and client associated given,
     * and also with a status that is different from the one given.
     * Then return an optional Ticket object.
     *
     * @param cardType the card type for the ticket looked.
     * @param state    the state of the ticket that is not the one that the ticket
     *                 looked has.
     * @param client   the client that made that ticket.
     * @return An Optional ticket object in any case.
     */
    @Query("FROM Ticket WHERE cardType=?1 AND status<>?2 AND client.id = :#{#client.id}")
    Optional<Ticket> findTicketByCardTypeAndClientAndStatusNotLike(Ticket.enumTypesOfCards cardType,
                                                                   Ticket.enumStatesOfTicket state,
                                                                   @Param("client") Client client);

    /**
     * Get the list of ticket that matches with the manager given and
     * also the state given.
     *
     * @param state   the status of the tickets wanted.
     * @param manager the manager in charge of the tickets looked.
     * @return A list with all the ticket with the given status and manager.
     * Or an empty list if there's no ticket that matches the
     */
    @Query("FROM Ticket WHERE status=?1 AND manager.id = :#{#manager.id}")
    List<Ticket> getTicketByClientAndStatusLike(Ticket.enumStatesOfTicket state,
                                                @Param("manager") Manager manager);

    /**
     * Given a manager and a state of the ticket, returns the total amount
     * of tickets with the requirements given.
     *
     * @param state   the state of the tickets that want to be counted.
     * @param manager the manager in charge of the ticket.
     * @return the number of tickets with the state and manager given.
     */
    @Query(value = "SELECT count(id) FROM Ticket WHERE status=?1 AND manager.id = :#{#manager.id}")
    Long countByStatusAndManager(Ticket.enumStatesOfTicket state,
                                 @Param("manager") Manager manager);

    /**
     * Given and id, search for a ticket with that id and returns it.
     *
     * @param id the Id of the Ticket wanted.
     * @return A ticket with the id given. Null if there's no ticket with that id.
     */
    @Query("FROM Ticket t WHERE t.id=?1")
    Ticket getTicketById(Long id);

    /**
     * Search for a ticket with the given id in the database,
     * and returns and optional for checking if exists.
     *
     * @param id the id of the ticket looked.
     * @return An Optional object in any case.
     */
    @Query("FROM Ticket t WHERE t.id=?1")
    Optional<Ticket> findTicketById(Long id);
}
