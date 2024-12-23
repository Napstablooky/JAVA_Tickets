package fr.isen.ticketapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper; // Ajout de l'import pour ObjectMapper
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import fr.isen.ticketapp.interfaces.services.UserService;
import fr.isen.ticketapp.impl.services.UserServiceImpl;
import fr.isen.ticketapp.interfaces.models.UserModel;
import java.util.List;

@Path("/user")
public class UserResource {
    private UserService userService;

    public UserResource() {
        this.userService = new UserServiceImpl();
    }

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> GetUsers() throws JsonProcessingException {
        List<UserModel> Users = this.userService.getUsers();
        return Uni.createFrom().item(() -> Response.ok(Users).build());
    }

    @Path("/one")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getUserById(@QueryParam("id") int id) {
        return Uni.createFrom()
                .item(() -> {
                    try {
                        UserModel userModel = this.userService.getUserById(id);
                        return Response.ok(userModel).build();
                    } catch (Exception e) {
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                .entity("Erreur interne : " + e.getMessage())
                                .build();
                    }
                });
    }

    @Path("/del")
    @DELETE
    public String removeUser(@QueryParam("id") int id) throws JsonProcessingException {
        // Supposons que la suppression de l'utilisateur se fait ici
        return "L'Utilisateur " + id + " a bien été supprimé.";
    }

    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String addUser(UserModel user) {
        // Vous pouvez ajouter un utilisateur ici
        return "L'Utilisateur a bien été ajouté.";
    }

    @Path("/update")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public String updateUser(UserModel user) {
        // Mise à jour de l'utilisateur
        return "L'Utilisateur numéro " + user.id + " a bien été mis à jour.";
    }
}
