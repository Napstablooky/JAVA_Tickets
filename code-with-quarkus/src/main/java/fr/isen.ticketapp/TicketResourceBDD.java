package fr.isen.ticketapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.isen.ticketapp.impl.services.TicketServiceImpl;
import fr.isen.ticketapp.interfaces.models.TicketModel;
import fr.isen.ticketapp.interfaces.services.TicketService;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
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

    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTicketBDD(TicketModel ticket) {
        try {
            ticketService.addTicketBDD(ticket);
            return Response.status(Response.Status.CREATED)
                    .entity("Le ticket a été ajouté avec succès.")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de l'ajout du ticket : " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTicket(TicketModel ticket) {
        try {
            ticketService.updateTicketBDD(ticket);
            return Response.ok("Le ticket a été mis à jour avec succès.").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la mise à jour du ticket : " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTicket(@QueryParam("id") int id) {
        try {
            ticketService.removeTicketBDD(id);
            return Response.ok("Le ticket avec l'ID " + id + " a été supprimé avec succès.").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la suppression du ticket : " + e.getMessage())
                    .build();
        }
    }
}
