package fr.isen.ticketapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.isen.ticketapp.impl.services.UserServiceImpl;
import fr.isen.ticketapp.interfaces.models.UserModel;
import fr.isen.ticketapp.interfaces.services.UserService;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.auth.User;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/userBDD")
public class UserResourceBDD {
    private UserService userService;

    public UserResourceBDD() {
        this.userService = new UserServiceImpl();
    }

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getUsersBDD() throws JsonProcessingException {
        List<UserModel> devices = this.userService.getUsersBDD();
        return Uni.createFrom().item(Response.ok(devices).build());
    }

    @Path("/one")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getUserByIdBDD(@QueryParam("id") int id) {
        return Uni.createFrom()
                .item(() -> {
                    try {
                        UserModel device = this.userService.getUserByIdBDD(id);
                        return Response.ok(device).build();
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
    public Response addUserBDD(UserModel user) {
        try {
            userService.addUserBDD(user);
            return Response.status(Response.Status.CREATED)
                    .entity("Le user a été ajouté avec succès.")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de l'ajout du user : " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(UserModel user) {
        try {
            userService.updateUserBDD(user);
            return Response.ok("Le user a été mis à jour avec succès.").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la mise à jour du user : " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@QueryParam("id") int id) {
        try {
            // Appel au service pour supprimer le device
            userService.removeUserBDD(id);

            // Retourne une réponse de succès
            return Response.ok("Le user avec l'ID " + id + " a été supprimé avec succès.").build();
        } catch (Exception e) {
            // Gestion des erreurs
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la suppression du device : " + e.getMessage())
                    .build();
        }
    }
}
