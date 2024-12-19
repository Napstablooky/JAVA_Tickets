package fr.isen.ticketapp.impl.services;

import fr.isen.ticketapp.interfaces.models.TicketModel;
import fr.isen.ticketapp.interfaces.models.enums.IMPACT;
import fr.isen.ticketapp.interfaces.services.TicketService;

import java.util.List;

public class TicketServiceImpl implements TicketService {

    @Override
    public List<TicketModel> getTickets() {
        return null;
    }

    @Override
    public TicketModel getTicketById(int id) {
        TicketModel ticket = new TicketModel();
        ticket.titre = "TEST";
        ticket.impact = IMPACT.Bloquant;
        return ticket;
    }

    @Override
    public TicketModel addTicket(TicketModel ticket) {
        return null;
    }

    @Override
    public TicketModel removeTicket(int id) {
        
        return null;
    }

    @Override
    public Integer updateTicket(TicketModel ticket) {
        return 0;
    }
}
