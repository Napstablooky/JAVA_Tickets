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
import javax.print.attribute.standard.Media;
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
        List<TicketModel> tickets = this.ticketService.getTickets();
        return Uni.createFrom().item(Response.ok(tickets).build());
    }

    @Path("/one")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getTicketById(@QueryParam("id") int id) {
        return Uni.createFrom()
                .item(() -> {
                    try {
                        TicketModel ticketModel = this.ticketService.getTicketById(id);
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
    public String removeTicket(@QueryParam("id") int id) throws JsonProcessingException {
        return "Le ticket " + id + " a bien été supprimé.";
    }

    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String addTicket(TicketModel ticket) {
        return "Le ticket a bien été ajouté.";
    }

    @Path("/update")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public String updateTicket(TicketModel ticket) {
        return "Le ticket numéro " + ticket.id + " a bien été mis à jour.";
    }
}