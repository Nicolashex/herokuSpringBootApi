/*
 * Copyright (c) 2021. Wings Design.
 */
package com.wings.designs.ProyectoFraude.persistence.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Represents a ticket for a fraudulent case made for an user.
 *
 * @author Nicolas Henriquez
 * @author Sebastian Zapata
 * @author Ignacio Cabrera
 * @author Maikol Leiva
 * @version 1.0
 */
@Entity(name = "Ticket")
public class Ticket implements Comparable<Ticket> {
    /**
     * It's the identifier of the ticket on the database.
     */
    @Id
    @SequenceGenerator(name = "ticket_sequence", sequenceName =
            "ticket_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "ticket_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    /**
     * It's the name of the type of card assigned with the ticket.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "card_type")
    private enumTypesOfCards cardType;

    /**
     * It's the comment made by the client about the ticket.
     */
    @Column(name = "comment", updatable = false)
    private String comment;

    /**
     * It's the current status of the ticket.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private enumStatesOfTicket status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "end_date")
    private LocalDate endDate;
    /**
     * It's the client that made the ticket.
     */
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    /**
     * It's the manager that took the ticket.
     */
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    /**
     * Empty Constructor.
     */
    public Ticket() {
        // Nothing here.
    }

    /**
     * Main constructor that receives all attributes as parameters,
     * except for the id and managerRut.
     *
     * @param client   Is the id of the user that made the ticket.
     * @param cardType Is the type of the card that was affected
     *                 by the fraud.
     * @param comment  Is a comment given by the client that contains a
     *                 little info about the incident.
     * @param status   Is the current status of the ticket.
     */
    public Ticket(final enumTypesOfCards cardType, final String comment,
                  final enumStatesOfTicket status, final Client client) {
        this.cardType = cardType;
        this.comment = comment;
        this.status = status;
        this.client = client;
        this.manager = null;
        this.startDate = LocalDate.now();
    }

    /**
     * Returns the id of the ticket.
     *
     * @return the id of the ticket.
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the type of card associated with the ticket.
     *
     * @return the type of card.
     */
    public enumTypesOfCards getCardType() {
        return cardType;
    }

    /**
     * Returns the comment made by the client when he made the ticket.
     *
     * @return the comment made by the client.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Returns the current status of the ticket.
     *
     * @return the current status of the ticket.
     */
    public enumStatesOfTicket getStatus() {
        return status;
    }

    /**
     * Change the status of the ticket with the given one.
     *
     * @param status the new status of the ticket.
     */
    public void setStatus(final enumStatesOfTicket status) {
        this.status = status;
    }

    /**
     * Returns the client that made the ticket.
     *
     * @return the client who made the ticket.
     */
    public Client getClient() {
        return client;
    }

    /**
     * Returns the manager who took the ticket.
     *
     * @return the manager who took the ticket. If the ticket has no manager
     * assigned yet, then returns null.
     */
    public Manager getManager() {
        return manager;
    }

    /**
     * Set the manager on charge of the ticket with the given one.
     *
     * @param manager the manager that is now in charge of the ticket.
     */
    public void setManager(final Manager manager) {
        this.manager = manager;
    }

    /**
     * Returns the date in which the ticket was created
     * by a client
     *
     * @return the creation date if the ticket has one or
     * null in the other case.
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Returns the date in which the ticket was finally
     * closed by a manager
     *
     * @return the end date if the ticket has one or
     * null in the other case.
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Set the end date of the ticket with
     * the given date.
     *
     * @param endDate the end date of the ticket.
     */
    public void setEndDate(final LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Compares the id of this ticket with the given ticket id,
     * then returns an integer that represents with one have a
     * greater id.
     *
     * @param o ticket to be compared by id.
     * @return a negative integer, zero, or a positive integer as this Ticket
     * is less than, equal to, or greater than the specified Ticket.
     */
    @Override
    public int compareTo(final Ticket o) {
        return this.getId().compareTo(o.getId());
    }


    /**
     * Enum that defines all the possibles states of the ticket.
     */
    public enum enumStatesOfTicket {
        /**
         * This status means that the ticket haven't been taken by a manager.
         */
        OPEN,

        /**
         * This status means that the ticket was taken by a manager
         * but still under review.
         */
        PENDING,

        /**
         * This status means that the ticket was finally resolved by a manager.
         */
        CLOSED
    }

    /**
     * Enum that defines the possibles types of a bank's card.
     */
    public enum enumTypesOfCards {
        /**
         * Represents that the card is a debit card of the bank.
         */
        CREDIT,

        /**
         * Represents that the card is a credit card of the bank.
         */
        DEBIT
    }
}
