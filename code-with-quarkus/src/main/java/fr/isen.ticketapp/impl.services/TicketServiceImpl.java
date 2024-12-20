package fr.isen.ticketapp.impl.services;

import fr.isen.ticketapp.interfaces.models.TicketModel;
import fr.isen.ticketapp.interfaces.models.enums.IMPACT;
import fr.isen.ticketapp.interfaces.services.TicketService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TicketServiceImpl implements TicketService {

    // Méthode pour lire un fichier JSON
    private String readFromJsonFile(String filePath) throws IOException {
        // Utilise java.nio.file pour lire le fichier
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    @Override
    public List<TicketModel> getTickets() { {
        List<TicketModel> tickets = null;
        try {
            // Lecture du contenu du fichier JSON depuis le dossier resources
            String rawJSON = readFromJsonFile("src/main/resources/Ticket.json");
            JSONParser parser = new JSONParser();
            JSONObject tickets = (List<TicketModel>) parser.parse(rawJSON);
            tickets = new ObjectMapper().readValue(rawJSON,List<TicketModel>);
            return tickets;
        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur lors de la lecture du fichier JSON : " + e.getMessage();
        }
    }

    @Override
    public TicketModel getTicketById(int id) {
        List<TicketModel> tickets = getTickets();
        ticket.titre = "TEST";
        ticket.impact = IMPACT.Bloquant;
        return ticket;
    }

    @Override
    public TicketModel addTicket(TicketModel ticket) {
        return null;
    }

    @Override
    public TicketModel removeTicket(int id) {

        return null;
    }

    @Override
    public Integer updateTicket(TicketModel ticket) {
        return 0;
    }
}
