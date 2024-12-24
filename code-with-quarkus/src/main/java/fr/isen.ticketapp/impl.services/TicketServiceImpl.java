package fr.isen.ticketapp.impl.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.isen.ticketapp.interfaces.models.TicketModel;
import fr.isen.ticketapp.interfaces.models.enums.ETATTICKET;
import fr.isen.ticketapp.interfaces.models.enums.IMPACT;
import fr.isen.ticketapp.interfaces.services.TicketService;
import jakarta.enterprise.inject.spi.CDI;
import io.agroal.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
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



    AgroalDataSource dataSource = CDI.current().select(AgroalDataSource.class).get();

    @Override
    public List<TicketModel> getTicketsBDD() {
        List<TicketModel> tickets = new ArrayList<>(); // Initialisation de la liste
        try (Connection conn = dataSource.getConnection()) {
            // Préparation de la requête SQL
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id, impact, titre, description, date_crea, etat, date_maj, type_demande, createur_id, poste_info_id FROM ticket")) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        // Création d'un nouvel objet TicketModel
                        TicketModel ticket = new TicketModel();
                        ticket.id = rs.getInt("id");
                        ticket.impact = IMPACT.valueOf(rs.getString("impact")); // Conversion en énumération
                        ticket.titre = rs.getString("titre");
                        ticket.description = rs.getString("description");
                        ticket.date_crea = rs.getDate("date_crea");
                        ticket.etat = ETATTICKET.valueOf(rs.getString("etat")); // Conversion en énumération
                        ticket.date_maj = rs.getDate("date_maj");
                        ticket.type_demande = rs.getString("type_demande");
                        ticket.createur_id = rs.getInt("createur_id");
                        ticket.poste_info_id = rs.getInt("poste_info_id");

                        // Ajout de l'objet à la liste
                        tickets.add(ticket);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des tickets : " + e.getMessage(), e);
        }

        return tickets; // Retourne la liste des tickets
    }

    @Override
    public TicketModel getTicketByIdBDD(int id) {
        TicketModel ticketModel = new TicketModel();
        try (Connection conn = dataSource.getConnection()) {
            // Ticket principal
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id, impact, titre, description, date_crea, etat, date_maj, createur_id, poste_info_id, type_demande FROM ticket WHERE id = ?")) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        ticketModel.id = rs.getInt("id");
                        ticketModel.impact = IMPACT.valueOf(rs.getString("impact"));
                        ticketModel.titre = rs.getString("titre");
                        ticketModel.description = rs.getString("description");
                        ticketModel.date_crea = rs.getDate("date_crea");
                        ticketModel.etat = ETATTICKET.valueOf(rs.getString("etat"));
                        ticketModel.date_maj = rs.getDate("date_maj");
                        ticketModel.type_demande = rs.getString("type_demande");
                        ticketModel.createur_id = rs.getInt("createur_id");
                        ticketModel.poste_info_id = rs.getInt("poste_info_id");
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du ticket : " + e.getMessage(), e);
        }

        return ticketModel;
    }

    @Override
    public void addTicketBDD(TicketModel ticket) {
        String query = "INSERT INTO ticket (impact, titre, description, date_crea, etat, date_maj, type_demande, createur_id, poste_info_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Définir les paramètres de la requête
            stmt.setString(1, ticket.impact.toString()); // Convertir l'impact en String si c'est une énumération
            stmt.setString(2, ticket.titre);
            stmt.setString(3, ticket.description);
            stmt.setDate(4, new Date(ticket.date_crea.getTime())); // Conversion de java.util.Date en java.sql.Date
            stmt.setString(5, ticket.etat.toString()); // Convertir l'état en String si c'est une énumération
            stmt.setDate(6, new Date(ticket.date_maj.getTime()));
            stmt.setString(7, ticket.type_demande);
            stmt.setInt(8, ticket.createur_id); // ID du créateur
            stmt.setInt(9, ticket.poste_info_id);

            // Exécution de la requête
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Le ticket a été ajouté avec succès !");
            } else {
                System.out.println("Échec de l'ajout du ticket.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout du ticket : " + e.getMessage(), e);
        }
    }

    @Override
    public void updateTicketBDD(TicketModel ticket) {
        String query = "UPDATE ticket SET impact = ?, titre = ?, description = ?, date_crea = ?, etat = ?, date_maj = ?, type_demande = ?, createur_id = ?, poste_info_id = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Définir les paramètres de la requête
            stmt.setString(1, ticket.impact.toString()); // Convertir l'impact en String si c'est une énumération
            stmt.setString(2, ticket.titre);
            stmt.setString(3, ticket.description);
            stmt.setDate(4, new java.sql.Date(ticket.date_crea.getTime())); // Conversion de java.util.Date en java.sql.Date
            stmt.setString(5, ticket.etat.toString()); // Convertir l'état en String si c'est une énumération
            stmt.setDate(6, new java.sql.Date(ticket.date_maj.getTime()));
            stmt.setString(7, ticket.type_demande);
            stmt.setInt(8, ticket.createur_id); // ID du créateur
            stmt.setInt(9, ticket.poste_info_id); // ID du poste info
            stmt.setInt(10, ticket.id); // ID du ticket à mettre à jour

            // Exécution de la requête
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Le ticket a été mis à jour avec succès !");
            } else {
                System.out.println("Échec de la mise à jour : aucun ticket trouvé avec l'ID " + ticket.id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du ticket : " + e.getMessage(), e);
        }
    }

    @Override
    public void removeTicketBDD(int id) {
        String query = "DELETE FROM ticket WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Définir le paramètre ID
            stmt.setInt(1, id);

            // Exécuter la requête
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Le ticket avec l'ID " + id + " a été supprimé avec succès !");
            } else {
                System.out.println("Aucun ticket trouvé avec l'ID " + id + ".");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du ticket : " + e.getMessage(), e);
        }
    }

}
