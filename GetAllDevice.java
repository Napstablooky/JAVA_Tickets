package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/GetAllDevice") // La route principale
public class GetAllDevice {

    @GET // Méthode temporaire pour rendre la route visible
    @Produces(MediaType.TEXT_PLAIN)
    public String placeholder() {
        return "Cette route est en cours de développement.";
    }
}
