/*
 * Copyright (c) 2021. Wings Design.
 */

package com.wings.designs.ProyectoFraude.service.notification;

import com.wings.designs.ProyectoFraude.persistence.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * The service that provides all the method related to
 * send emails.
 */
@Service
public class NotificationService {
    /**
     * Class that allows to send emails.
     */
    private final JavaMailSender javaMailSender;

    /**
     * The  Class that allows to send emails.
     */
    private final NotificationConfig notificationConfig;

    /**
     * Main constructor.
     *
     * @param javaMailSender     Class that allows to send emails.
     * @param notificationConfig Class that allows to send emails.
     */
    @Autowired
    public NotificationService(final JavaMailSender javaMailSender,
                               final NotificationConfig notificationConfig) {
        this.javaMailSender = javaMailSender;
        this.notificationConfig = notificationConfig;
    }

    /**
     * Given a ticket, send and email to notify the client
     * of that ticket that it's now closed.
     *
     * @param ticket the ticket closed that has the information
     *               needed in the email sent.
     * @throws MailException If there's an error sending the email.
     */
    public void sendNotificationToClient(final Ticket ticket)
            throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(ticket.getClient().getEmail());
        mail.setFrom(notificationConfig.getUsername());
        String subject = "Your ticket has been closed";
        mail.setSubject(subject);
        String text = "Congratulations: " + ticket.getClient().getFullName()
                + "the ticket with the id: " + ticket.getId()
                + "associated with your" + ticket.getCardType()
                + "has been closed by the manager: "
                + ticket.getManager().getFullName();
        mail.setText(text);
        javaMailSender.send(mail);

    }
}
