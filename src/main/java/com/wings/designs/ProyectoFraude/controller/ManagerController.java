/*
 * Copyright (c) 2021. Wings Design.
 */

package com.wings.designs.ProyectoFraude.controller;

import com.wings.designs.ProyectoFraude.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The rest Controller in charge of the manager
 * resource's endpoints on the API.
 *
 * @author Nicolas Henriquez
 * @author Sebastian Zapata
 * @author Ignacio Cabrera
 * @author Maikol Leiva
 * @version 1.0
 */
@RestController
@RequestMapping("/managers")
public class ManagerController {

    /**
     * To make changes or request to the manager table
     * in the database through the methods of this service.
     */
    private final ManagerService managerService;

    /**
     * Main constructor. Autowired.
     *
     * @param managerService service used to retrieve data
     *                       from the manager table in the
     *                       database.
     */
    @Autowired
    public ManagerController(final ManagerService managerService) {
        this.managerService = managerService;
    }

    /**
     * @param id       the Id of the manager.
     * @param response Object used to save and send information
     *                 to the client.
     * @throws IOException when theres an error while making the pdf.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/tickets/export")
    public void exportManagerReport(@PathVariable final Long id,
                                    final HttpServletResponse response)
            throws IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename= report.pdf";
        response.setHeader(headerKey, headerValue);
        managerService.exportPdf(response, id);

    }

}
