package fr.isen.ticketapp.impl.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import fr.isen.ticketapp.interfaces.models.ConfigurationModel;
import fr.isen.ticketapp.interfaces.models.PosteInfoModel;
import fr.isen.ticketapp.interfaces.models.TicketModel;
import fr.isen.ticketapp.interfaces.models.UserModel;
import fr.isen.ticketapp.interfaces.models.enums.ETATPOSTE;
import fr.isen.ticketapp.interfaces.models.enums.ETATTICKET;
import fr.isen.ticketapp.interfaces.models.enums.IMPACT;
import fr.isen.ticketapp.interfaces.models.enums.ROLE;
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



    AgroalDataSource dataSource = CDI.current().select(AgroalDataSource.class).get();

    @Override
    public List<TicketModel> getTicketsBDD() {
        List<TicketModel> tickets = new ArrayList<>(); // Initialisation de la liste
        try (Connection conn = dataSource.getConnection()) {
            // Préparation de la requête SQL
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id, impact, titre, description, date_crea, etat, date_maj, type_demande FROM ticket")) {
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
                    }
                }
            }

            // Créateur (User)
            UserModel user = new UserModel();
            try (PreparedStatement stmt2 = conn.prepareStatement(
                    "SELECT nom, email, password, last_connexion, statut, role FROM user WHERE id = ?")) {
                stmt2.setInt(1, ticketModel.id); // Utilise l'id du créateur
                try (ResultSet rs2 = stmt2.executeQuery()) {
                    if (rs2.next()) {
                        user.id = ticketModel.id;
                        user.nom = rs2.getString("nom");
                        user.email = rs2.getString("email");
                        user.password = rs2.getString("password");
                        user.last_connexion = rs2.getDate("last_connexion");
                        user.statut = rs2.getBoolean("statut");
                        user.role = ROLE.valueOf(rs2.getString("role"));
                    }
                }
            }
            ticketModel.createur = user;

            // Configuration
            ConfigurationModel config = new ConfigurationModel();
            try (PreparedStatement stmt3 = conn.prepareStatement(
                    "SELECT processeur, RAM, stockage, gpu, os, ecran FROM configuration WHERE id = ?")) {
                stmt3.setInt(1, 1); // ID de configuration
                try (ResultSet rs3 = stmt3.executeQuery()) {
                    if (rs3.next()) {
                        config.processeur = rs3.getString("processeur");
                        config.RAM = rs3.getString("RAM");
                        config.stockage = rs3.getString("stockage");
                        config.gpu = rs3.getString("gpu");
                        config.os = rs3.getString("os");
                        config.ecran = rs3.getString("ecran");
                    }
                }
            }

            // Poste Info
            PosteInfoModel device = new PosteInfoModel();
            try (PreparedStatement stmt4 = conn.prepareStatement(
                    "SELECT etat FROM poste_info WHERE id = ?")) {
                stmt4.setInt(1, ticketModel.id); // ID du poste info
                try (ResultSet rs4 = stmt4.executeQuery()) {
                    if (rs4.next()) {
                        device.etat = ETATPOSTE.valueOf(rs4.getString(1));
                    }
                }
            }
            device.id = ticketModel.id;
            device.user = user;
            device.configuration = config;
            ticketModel.poste_info = device;

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du ticket : " + e.getMessage(), e);
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
