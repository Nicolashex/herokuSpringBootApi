/*
 * Copyright (c) 2021. Wings Design.
 */

package com.wings.designs.ProyectoFraude.requestbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wings.designs.ProyectoFraude.persistence.model.Ticket;

/**
 * Represent a Request with all the data needed to make a ticket in
 * form of his attributes.
 */
public class NewTicketRequest {
    /**
     * The card type of the ticket.
     */
    @JsonProperty("card_type")
    private Ticket.enumTypesOfCards cardType;

    /**
     * the comment given by the client for the ticket.
     */
    private String comment;

    /**
     * Empty Constructor.
     */
    public NewTicketRequest() {
        // Nothing here.
    }

    /**
     * Main constructor.
     *
     * @param cardType the card type of the Ticket Request.
     * @param comment  the comment of the Ticket Request.
     */
    public NewTicketRequest(final Ticket.enumTypesOfCards cardType,
                            final String comment) {
        this.cardType = cardType;
        this.comment = comment;
    }

    /**
     * Return the card type of the ticket request.
     *
     * @return the card type of the Ticket request. Null if there's
     * no card type.
     */
    public Ticket.enumTypesOfCards getCardType() {
        return cardType;
    }

    /**
     * Returns the comment of the ticket request.
     *
     * @return the comment on the ticket request. Null if there's no
     * comment.
     */
    public String getComment() {
        return comment;
    }
}
