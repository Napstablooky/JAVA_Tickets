package fr.isen.ticketapp;


import fr.isen.ticketapp.interfaces.models.PosteInfoModel;
import fr.isen.ticketapp.interfaces.services.DeviceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import fr.isen.ticketapp.impl.services.DeviceServiceImpl;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/deviceJSON")
public class DeviceResource {
    private DeviceService deviceService;

    public DeviceResource() {
        this.deviceService = new DeviceServiceImpl();
    }

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getDevices() throws JsonProcessingException {
        List<PosteInfoModel> devices = this.deviceService.getDevices();
        return Uni.createFrom().item(Response.ok(devices).build());
    }

    @Path("/one")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getDeviceById(@QueryParam("id") int id) {
        return Uni.createFrom().item(() -> {
            try {
                PosteInfoModel device = this.deviceService.getDeviceById(id);
                return Response.ok(device).build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erreur interne : " + e.getMessage()).build();
            }
        });
    }

    @Path("/del")
    @DELETE
    public String removeDevice(@QueryParam("id") int id) throws JsonProcessingException {
        return "Le poste informatique numéro " + id + " a bien été supprimé.";
    }

    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String addDevice(PosteInfoModel device) {
        return "Le poste informatique a bien été ajouté.";
    }

    @Path("/update")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public String updateDevice(PosteInfoModel device) {
        return "Le poste informatique numéro " + device.id + " a bien été mis à jour.";
    }
}
