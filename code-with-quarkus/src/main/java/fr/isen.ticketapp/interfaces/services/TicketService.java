package fr.isen.ticketapp.interfaces.services;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.isen.ticketapp.interfaces.models.TicketModel;

public interface TicketService {
    String getTickets();

    String getTicketById(final int id) throws JsonProcessingException;

    TicketModel addTicket(final TicketModel ticket);

    TicketModel removeTicket(final int id);

    Integer updateTicket(final TicketModel ticket);

}
