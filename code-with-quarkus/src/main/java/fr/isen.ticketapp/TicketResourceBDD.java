package fr.isen.ticketapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.isen.ticketapp.impl.services.TicketServiceImpl;
import fr.isen.ticketapp.interfaces.models.TicketModel;
import fr.isen.ticketapp.interfaces.services.TicketService;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/ticketBDD")
public class TicketResourceBDD {
    private TicketService ticketService;

    public TicketResourceBDD() {
        this.ticketService = new TicketServiceImpl();
    }

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getTicketsBDD() throws JsonProcessingException {
        List<TicketModel> tickets = this.ticketService.getTicketsBDD();
        return Uni.createFrom().item(Response.ok(tickets).build());
    }

    @Path("/one")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getTicketByIdBDD(@QueryParam("id") int id) {
        return Uni.createFrom()
                .item(() -> {
                    try {
                        TicketModel ticket = this.ticketService.getTicketByIdBDD(id);
                        return Response.ok(ticket).build();
                    } catch (Exception e) {
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                .entity("Erreur interne : " + e.getMessage())
                                .build();
                    }
                });
    }
}
