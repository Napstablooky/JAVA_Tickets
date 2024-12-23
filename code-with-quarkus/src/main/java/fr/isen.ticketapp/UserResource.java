package fr.isen.ticketapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper; // Ajout de l'import pour ObjectMapper
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import fr.isen.ticketapp.interfaces.services.UserService;
import fr.isen.ticketapp.impl.services.UserServiceImpl;
import fr.isen.ticketapp.interfaces.models.UserModel;

@Path("/user")
public class UserResource {
    private UserService userService; // Nom corrigé avec une minuscule

    public UserResource() {
        this.userService = new UserServiceImpl(); // Nom corrigé
    }

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> GetUsers() {
        return Uni.createFrom().item(() -> {
            try {
                // Récupération de la liste des utilisateurs
                List<UserModel> users = this.userService.getUsers();

                // Conversion de la liste en JSON
                ObjectMapper objectMapper = new ObjectMapper();
                String usersJson = objectMapper.writeValueAsString(users);

                return Response.ok(usersJson).build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Erreur interne : " + e.getMessage())
                        .build();
            }
        });
    }


    @Path("/one")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getUserById(@QueryParam("id") int id) {
        return Uni.createFrom()
                .item(() -> {
                    try {
                        UserModel userModel = this.userService.getUserById(id); // Nom corrigé
                        if (userModel == null) {
                            return Response.status(Response.Status.NOT_FOUND)
                                    .entity("L'Utilisateur avec l'ID " + id + " est introuvable.")
                                    .build();
                        }
                        // Convertir l'objet UserModel en JSON
                        ObjectMapper objectMapper = new ObjectMapper();
                        String userJson = objectMapper.writeValueAsString(userModel);

                        return Response.ok(userJson).build();
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
        return "L'Utilisateur " + id + " a bien été supprimé.";
    }

    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String addUser(UserModel user) {
        return "L'Utilisateur a bien été ajouté.";
    }

    @Path("/update")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public String updateUser(UserModel user) {
        return "L'Utilisateur numéro " + user.id + " a bien été mis à jour.";
    }
}
