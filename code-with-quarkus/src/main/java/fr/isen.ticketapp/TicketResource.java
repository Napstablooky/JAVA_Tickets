package fr.isen.ticketapp;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Path("/ticket")
public class TicketResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String GetTickets() {
        try {
            // Lecture du contenu du fichier JSON depuis le dossier resources
            String rawJSON = readFromJsonFile("src/main/resources/Ticket.json");
            return rawJSON;
        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur lors de la lecture du fichier JSON : " + e.getMessage();
        }
    }

    // Méthode pour lire un fichier JSON
    private String readFromJsonFile(String filePath) throws IOException {
        // Utilise java.nio.file pour lire le fichier
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}