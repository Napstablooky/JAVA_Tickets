package fr.isen.ticketapp.impl.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.isen.ticketapp.interfaces.models.PosteInfoModel;
import fr.isen.ticketapp.interfaces.models.enums.ETATPOSTE;
import fr.isen.ticketapp.interfaces.services.DeviceService;
import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.inject.spi.CDI;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeviceServiceImpl implements DeviceService {
    private String readFromJsonFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    @Override
    public List<PosteInfoModel> getDevices() throws JsonProcessingException {
        {
            // Lecture du contenu du fichier JSON depuis le dossier resources
            String rawJSON;
            try {
                rawJSON = readFromJsonFile("src/main/resources/Poste_Informatique.json");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ObjectMapper objectMapper = new ObjectMapper();

            // Lecture de la liste de tickets
            List<PosteInfoModel> devices = objectMapper.readValue(rawJSON, new TypeReference<List<PosteInfoModel>>() {});

            return devices;
        }
    }

    @Override
    public PosteInfoModel getDeviceById(int id) throws JsonProcessingException {
        String rawJSON;
        try {
            rawJSON = Files.readString(Paths.get("src/main/resources/Poste_Informatique.json"));
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture du fichier JSON", e);
        }

        // Instanciation de l'ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        // Lecture de la liste de tickets
        List<PosteInfoModel> devices = objectMapper.readValue(rawJSON, new TypeReference<List<PosteInfoModel>>() {});

        for (PosteInfoModel device : devices) {
            if (device.id == id) {
                return device; 
            }
        }

        throw new RuntimeException("Device avec l'ID " + id + " introuvable.");
    }

    @Override
    public PosteInfoModel addOneDevice(PosteInfoModel device) {
        return null;
    }

    @Override
    public PosteInfoModel removeOneDevice(int id) {

        return null;
    }

    @Override
    public Integer updateOneDevice(PosteInfoModel device) {
        return 0;
    }

    AgroalDataSource dataSource = CDI.current().select(AgroalDataSource.class).get();

    @Override
    public List<PosteInfoModel> getDevicesBDD() {
        List<PosteInfoModel> devices = new ArrayList<>(); // Initialisation de la liste
        try (Connection conn = dataSource.getConnection()) {
            // Préparation de la requête SQL
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id, user_id, etat, configuration_id FROM poste_info")) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        // Création d'un nouvel objet TicketModel
                        PosteInfoModel device = new PosteInfoModel();
                        device.id = rs.getInt("id");
                        device.user_id = rs.getInt("user_id");
                        device.etat = ETATPOSTE.valueOf(rs.getString("etat"));
                        device.configuration_id = rs.getInt("configuration_id");

                        // Ajout de l'objet à la liste
                        devices.add(device);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des devices : " + e.getMessage(), e);
        }

        return devices; // Retourne la liste des tickets
    }

    @Override
    public PosteInfoModel getDeviceByIdBDD(int id) {
        PosteInfoModel device = new PosteInfoModel();
        try (Connection conn = dataSource.getConnection()) {
            // Ticket principal
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id, user_id, etat, configuration_id FROM poste_info WHERE id = ?")) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        device.id = rs.getInt("id");
                        device.user_id = rs.getInt("user_id");
                        device.etat = ETATPOSTE.valueOf(rs.getString("etat"));
                        device.configuration_id = rs.getInt("configuration_id");
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du device : " + e.getMessage(), e);
        }

        return device;
    }

    @Override
    public void addDeviceBDD(PosteInfoModel device) {
        String query = "INSERT INTO poste_info (user_id, etat, configuration_id) "
                + "VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, device.user_id);
            stmt.setString(2, device.etat.toString());
            stmt.setInt(3,device.configuration_id);


            // Exécution de la requête
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Le device a été ajouté avec succès !");
            } else {
                System.out.println("Échec de l'ajout du device.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout du device : " + e.getMessage(), e);
        }
    }

    @Override
    public void updateDeviceBDD(PosteInfoModel device) {
        String query = "UPDATE poste_info SET user_id = ?, etat = ?, configuration_id = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, device.user_id);
            stmt.setString(2, device.etat.toString());
            stmt.setInt(3, device.configuration_id);
            stmt.setInt(4, device.id);

            // Exécution de la requête
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Le device a été mis à jour avec succès !");
            } else {
                System.out.println("Échec de la mise à jour : aucun device trouvé avec l'ID " + device.id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du device : " + e.getMessage(), e);
        }
    }

    @Override
    public void removeDeviceBDD(int id) {
        String query = "DELETE FROM poste_info WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Définir le paramètre ID
            stmt.setInt(1, id);

            // Exécuter la requête
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Le device avec l'ID " + id + " a été supprimé avec succès !");
            } else {
                System.out.println("Aucun device trouvé avec l'ID " + id + ".");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du ticket : " + e.getMessage(), e);
        }
    }
}
