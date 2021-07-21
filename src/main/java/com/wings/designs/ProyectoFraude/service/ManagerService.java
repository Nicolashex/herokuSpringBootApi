/*
 * Copyright (c) 2021. Wings Design.
 */

package com.wings.designs.ProyectoFraude.service;

import com.wings.designs.ProyectoFraude.persistence.model.Manager;
import com.wings.designs.ProyectoFraude.persistence.model.Ticket;
import com.wings.designs.ProyectoFraude.persistence.model.User;
import com.wings.designs.ProyectoFraude.persistence.repository.ManagerRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Service that serves as a intermediary with the database,
 * defining the method that will be used to communicate with the
 * database.
 */
@Service
public class ManagerService {
    /**
     * Class that allows to make direct request to the database table
     * of managers.
     */
    private final ManagerRepository managerRepository;
    /**
     * Service that allow to make a pdf.
     */
    private final PdfService pdfService;

    /**
     *
     * @param managerRepository repository used to make
     *                          the request to the managers
     *                          table on the database.
     * @param pdfService Service used to make a pdf.
     */
    public ManagerService(final ManagerRepository managerRepository,
                          final PdfService pdfService) {
        this.managerRepository = managerRepository;
        this.pdfService = pdfService;
    }

    /**
     * Look for a manager with the given email and send
     * an optional back based if the manager with
     * the id exists or not.
     * @param email the email of the manager wanted.
     * @return An Optional object in any case.
     */
    public Optional<Manager> findManagerByEmail(final String email) {
        return this.managerRepository.findManagerByEmail(email);
    }

    /**
     * Returns a manager that possess the user given.
     * @param user the user of the manager wanted.
     * @return A manager if found, null if not.
     */
    public Manager getManagerByUser(final User user) {
        return managerRepository.getManagerByUser(user);
    }

    /**
     * Add the manager given to the database.
     * @param manager the manager to be added.
     */
    public void addNewManager(final Manager manager) {
        this.managerRepository.save(manager);
    }

    /**
     *
     * @param rut the rut of the manager wanted.
     * @return A manager if found, null if not.
     */
    public Manager getManagerByRut(final String rut) {
        return managerRepository.getManagerByRut(rut);
    }

    /**
     *
     * @param response The response given to the client.
     * @param managerId the id of the manager where the report is
     *                  based of.
     * @throws IOException
     */
    public void exportPdf(final HttpServletResponse response,
                          final Long managerId) throws IOException {
        Manager manager = managerRepository.getManagerById(managerId);
        List<Ticket> list = manager.getTicketList();
        this.pdfService.getManagerReport(list, manager.getFullName(), response);
    }
}
