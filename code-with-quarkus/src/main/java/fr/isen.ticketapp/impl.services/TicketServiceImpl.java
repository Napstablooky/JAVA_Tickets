package fr.isen.ticketapp.impl.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.isen.ticketapp.interfaces.models.TicketModel;
import fr.isen.ticketapp.interfaces.services.TicketService;
import jakarta.enterprise.inject.spi.CDI;
import io.agroal.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TicketServiceImpl implements TicketService {

    // Méthode pour lire un fichier JSON
    private String readFromJsonFile(String filePath) throws IOException {
        // Utilise java.nio.file pour lire le fichier
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    @Override
    public List<TicketModel> getTickets() throws JsonProcessingException {
        {
            // Lecture du contenu du fichier JSON depuis le dossier resources
            String rawJSON;
            try {
                rawJSON = readFromJsonFile("src/main/resources/Ticket.json");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ObjectMapper objectMapper = new ObjectMapper();

            // Lecture de la liste de tickets
            List<TicketModel> tickets = objectMapper.readValue(rawJSON, new TypeReference<List<TicketModel>>() {});

            return tickets;
        }
    }

    @Override
    public TicketModel getTicketById(int id) throws JsonProcessingException {
        String rawJSON;
        try {
            rawJSON = Files.readString(Paths.get("src/main/resources/Ticket.json"));
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture du fichier JSON", e);
        }

        // Instanciation de l'ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        // Lecture de la liste de tickets
        List<TicketModel> tickets = objectMapper.readValue(rawJSON, new TypeReference<List<TicketModel>>() {});

        for (TicketModel ticket : tickets) {
            if (ticket.id == id) {
                return ticket; // Ticket trouvé
            }
        }

        throw new RuntimeException("Ticket avec l'ID " + id + " introuvable.");
    }

    AgroalDataSource dataSource = CDI.current().select(AgroalDataSource.class).get();

    @Override
    public TicketModel getTicketByIdBDD(int id) {
        TicketModel ticketModel = new TicketModel();
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ticket WHERE id =?");
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                // USE factory here
                ticketModel.id = rs.getInt(1);
                ticketModel.titre = rs.getString(2);
            }

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ticketModel;
    }

    @Override
    public TicketModel addOneTicket(TicketModel ticket) {
        return null;
    }

    @Override
    public TicketModel removeOneTicket(int id) {

        return null;
    }

    @Override
    public Integer updateOneTicket(TicketModel ticket) {
        return 0;
    }
}
