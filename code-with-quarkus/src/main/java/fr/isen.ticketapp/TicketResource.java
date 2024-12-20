package fr.isen.ticketapp;

import fr.isen.ticketapp.impl.services.TicketServiceImpl;
import fr.isen.ticketapp.interfaces.models.TicketModel;
import fr.isen.ticketapp.interfaces.services.TicketService;
import io.smallrye.mutiny.Uni;
import jakarta.json.Json;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import javax.print.DocFlavor;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Path("/ticket")
public class TicketResource {

    private TicketService ticketService;

    public TicketResource() {
        this.ticketService = new TicketServiceImpl();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> GetTickets() {
        List<TicketModel> tickets = this.ticketService.getTickets();
        return Uni.createFrom().item(Response.ok(tickets).build());
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Uni<Response> getOneTicket(int id) {
        TicketModel ticketModel = this.ticketService.getTicketById(id);
        return Uni.createFrom().item(Response.ok(ticketModel).build());
    }

    @DELETE
    @Path("/{id}")
    public String DelTicketById(int id) {
        return "Le ticket " + id + "a bien été supprimé.";
    }
}