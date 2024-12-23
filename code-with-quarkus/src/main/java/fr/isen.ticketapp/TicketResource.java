package fr.isen.ticketapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.isen.ticketapp.impl.services.TicketServiceImpl;
import fr.isen.ticketapp.interfaces.models.TicketModel;
import fr.isen.ticketapp.interfaces.services.TicketService;
import io.smallrye.mutiny.Uni;
import jakarta.json.Json;
import jakarta.ws.rs.*;
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

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> GetTickets() throws JsonProcessingException {
        String tickets = this.ticketService.getTickets();
        return Uni.createFrom().item(Response.ok(tickets).build());
    }

    @Path("/one")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getTicketById(@QueryParam("id") int id) {
        return Uni.createFrom()
                .item(() -> {
                    try {
                        String ticketModel = this.ticketService.getTicketById(id);
                        if (ticketModel == null || ticketModel.isEmpty()) {
                            return Response.status(Response.Status.NOT_FOUND)
                                    .entity("Ticket avec l'ID " + id + " introuvable")
                                    .build();
                        }
                        return Response.ok(ticketModel).build();
                    } catch (Exception e) {
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                .entity("Erreur interne : " + e.getMessage())
                                .build();
                    }
                });
    }

    @Path("/del")
    @DELETE
    public String removeTicket(@QueryParam("id") int id) {
        return "Le ticket " + id + " a bien été supprimé.";
    }

    @Path("/add")
    @POST
    public String addTicket(@QueryParam("ticket") TicketModel ticket) {
        return "Le ticket a bien été ajouté.";
    }

    @Path("/update")
    @PUT
    public String updateTicket(@QueryParam("ticket") TicketModel ticket) {
        return "Le ticket numéro " + ticket.id + " a bien été mis à jour.";
    }
}