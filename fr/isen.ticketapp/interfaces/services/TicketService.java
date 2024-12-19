package fr.isen.ticketapp.interfaces.services;

import java.util.List;
import fr.isen.ticketapp.interfaces.models.TicketModel;

public interface TicketService {
    List<TicketModel> getTickets();

    TicketModel getTicketById(final int id);

    TicketModel addTicket(final TicketModel ticket);

    TicketModel removeTicket(final int id);

    Integer updateTicket(final TicketModel ticket);

}
