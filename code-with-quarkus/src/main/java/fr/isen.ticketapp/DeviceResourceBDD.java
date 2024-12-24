package fr.isen.ticketapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.isen.ticketapp.impl.services.DeviceServiceImpl;
import fr.isen.ticketapp.interfaces.models.PosteInfoModel;
import fr.isen.ticketapp.interfaces.services.DeviceService;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/deviceBDD")
public class DeviceResourceBDD {
    private DeviceService deviceService;

    public DeviceResourceBDD() {
        this.deviceService = new DeviceServiceImpl();
    }

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getDevicesBDD() throws JsonProcessingException {
        List<PosteInfoModel> devices = this.deviceService.getDevicesBDD();
        return Uni.createFrom().item(Response.ok(devices).build());
    }

    @Path("/one")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getDeviceByIdBDD(@QueryParam("id") int id) {
        return Uni.createFrom()
                .item(() -> {
                    try {
                        PosteInfoModel device = this.deviceService.getDeviceByIdBDD(id);
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
    public Response addDeviceBDD(PosteInfoModel device) {
        try {
            deviceService.addDeviceBDD(device);
            return Response.status(Response.Status.CREATED)
                    .entity("Le device a été ajouté avec succès.")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de l'ajout du device : " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDevice(PosteInfoModel device) {
        try {
            deviceService.updateDeviceBDD(device);
            return Response.ok("Le device a été mis à jour avec succès.").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la mise à jour du device : " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDevice(@QueryParam("id") int id) {
        try {
            deviceService.removeDeviceBDD(id);
            return Response.ok("Le device avec l'ID " + id + " a été supprimé avec succès.").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la suppression du device : " + e.getMessage())
                    .build();
        }
    }
}
