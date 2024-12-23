package fr.isen.ticketapp.interfaces.services;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.isen.ticketapp.interfaces.models.TicketModel;

public interface TicketService {
    List<TicketModel> getTickets() throws JsonProcessingException;

    TicketModel getTicketById(final int id) throws JsonProcessingException;

    TicketModel addOneTicket(final TicketModel ticket);

    TicketModel removeOneTicket(final int id);

    Integer updateOneTicket(final TicketModel ticket);

}
