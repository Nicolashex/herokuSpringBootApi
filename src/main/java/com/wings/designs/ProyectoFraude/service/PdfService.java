/*
 * Copyright (c) 2021. Wings Design.
 */

package com.wings.designs.ProyectoFraude.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.wings.designs.ProyectoFraude.persistence.model.Ticket;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PdfService {

    /**
     * Empty Constructor.
     */
    public PdfService() {
        //Nothing here.
    }

    /**
     * Given a table, write the headers needed to make
     * the manager report on pdf.
     * @param table the Pdf table where the headers will be.
     */
    public void writeTableHeader(final PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.ORANGE.darker());
        cell.setPadding(5);
        com.lowagie.text.Font font = FontFactory.getFont(FontFactory.COURIER);
        font.setColor(Color.WHITE);
        List<String> headers = new ArrayList<>();
        headers.add("ID");
        headers.add("TYPE OF CARD");
        headers.add("CLIENT RUT");
        headers.add("START DATE");
        headers.add("END DATE");
        headers.add("DAYS UNTIL CLOSED");
        for (String header : headers) {
            cell.setPhrase(new Phrase(header, font));
            table.addCell(cell);
        }
    }

    /**
     *
     * @param table the table where the data will be in.
     * @param tickets the tickets of the manager that the
     *                report is based of.
     * @return Return a list of Long values with statistics
     * of the report.
     */
    public List<Long> writeTableData(final PdfPTable table,
                                     final List<Ticket> tickets) {
        Long maxTimeTakenToResolve = 0L;
        Long averageTimeTakenToResolve = 0L;
        Long numberOfTickets = 0L;
        for (Ticket ticket : tickets) {
            Period period;
            if (ticket.getStatus().equals(Ticket.enumStatesOfTicket.CLOSED)) {
                table.addCell(String.valueOf(ticket.getId()));
                table.addCell(ticket.getCardType().name());
                table.addCell(ticket.getClient().getRut());
                table.addCell(ticket.getStartDate().toString());
                table.addCell(ticket.getEndDate().toString());
                LocalDate starDate = ticket.getStartDate();
                LocalDate endDate = ticket.getEndDate();
                period = Period.between(starDate, endDate);
                Long timeTaken = ChronoUnit.DAYS.between(starDate, endDate);
                if (timeTaken < 0) {
                    timeTaken = 0L;
                }
                table.addCell(String.valueOf(timeTaken));
                if (timeTaken > maxTimeTakenToResolve) {
                    maxTimeTakenToResolve = timeTaken;
                }
                numberOfTickets = numberOfTickets + 1L;
                averageTimeTakenToResolve = averageTimeTakenToResolve
                        + timeTaken;
            }
        }
        averageTimeTakenToResolve = averageTimeTakenToResolve / numberOfTickets;
        List<Long> list = new ArrayList<Long>();
        list.add(numberOfTickets);
        list.add(averageTimeTakenToResolve);
        list.add(maxTimeTakenToResolve);
        return list;

    }

    /**
     *
     * @param tickets the tickets of the manager.
     * @param name the name of the manager.
     * @param response the response needed to send the
     *                pdf to the client.
     * @throws IOException
     */
    public void getManagerReport(final List<Ticket> tickets,
                                 final String name,
                                 final HttpServletResponse response)
            throws IOException {
        Collections.sort(tickets);
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        document.add(new Paragraph(
                "REPORT OF CLOSED TICKETS OF "
                        + name.toUpperCase()));
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(20);
        table.setSpacingAfter(20);
        writeTableHeader(table);
        List<Long> list = writeTableData(table, tickets);
        document.add(table);
        document.add(new Paragraph(
                "Number of tickets: " + list.get(0) + "\n"
                        + "Average days taken to resolve: " + list.get(1) + "\n"
                        + "Maximum days taken to resolve: " + list.get(2) + "\n"));
        document.close();
    }
}
