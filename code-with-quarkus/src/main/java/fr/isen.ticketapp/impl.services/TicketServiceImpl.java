package fr.isen.ticketapp.impl.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.isen.ticketapp.interfaces.models.TicketModel;
import fr.isen.ticketapp.interfaces.models.enums.IMPACT;
import fr.isen.ticketapp.interfaces.services.TicketService;
import com.fasterxml.jackson.core.*;

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
    public String getTickets() {
        {
            // Lecture du contenu du fichier JSON depuis le dossier resources
            String rawJSON = null;
            try {
                rawJSON = readFromJsonFile("src/main/resources/Ticket.json");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return rawJSON;
        }
    }

    @Override
    public String getTicketById(int id) throws JsonProcessingException {
        String rawJSON = null;
        try {
            rawJSON = readFromJsonFile("src/main/resources/Ticket.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        List<TicketModel> tickets = objectMapper.readValue(rawJSON, new TypeReference<List<TicketModel>>() {});

        TicketModel ticket = null;
        for (TicketModel ticketP : tickets) {
            if (ticketP.id == id) {
                ticket = ticketP;
                break;
            }
        }

        ObjectMapper objectMapper2 = new ObjectMapper();
        String StringTicket = objectMapper2.writeValueAsString(ticket);
        System.out.println(StringTicket);

        return StringTicket;
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
