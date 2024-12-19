package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

@Path("/GetOneTicket")
public class GetOneTicket {

    @GET
    @Path("/{id}") // Endpoint avec un paramètre ID
    @Produces(MediaType.TEXT_PLAIN)
    public String getTicketById(@PathParam("id") String id) {
        try {
            // Lire tous les tickets depuis le fichier JSON
            List<Ticket> tickets = readTicketsFromJsonFile("src/main/resources/Ticket.json");

            // Chercher un ticket par son ID
            Optional<Ticket> ticket = tickets.stream()
                    .filter(t -> t.getId().equals(id))
                    .findFirst();

            // Retourner le ticket s'il existe
            if (ticket.isPresent()) {
                return "Ticket trouvé :\n" + ticket.get();
            } else {
                return "Aucun ticket trouvé avec l'ID : " + id;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur lors de la lecture du fichier JSON : " + e.getMessage();
        }
    }

    // Méthode pour lire et mapper les tickets depuis le fichier JSON
    private List<Ticket> readTicketsFromJsonFile(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        // Lire le contenu du fichier JSON
        String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));

        // Mapper l'objet JSON avec la clé "tickets"
        Map<String, List<Ticket>> ticketsWrapper = objectMapper.readValue(
                jsonContent,
                new TypeReference<Map<String, List<Ticket>>>() {}
        );

        // Retourner la liste des tickets
        return ticketsWrapper.get("tickets");
    }
}

