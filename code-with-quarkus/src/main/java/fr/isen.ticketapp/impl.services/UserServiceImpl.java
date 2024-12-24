package fr.isen.ticketapp.impl.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.isen.ticketapp.interfaces.models.UserModel;
import fr.isen.ticketapp.interfaces.models.enums.ROLE;
import fr.isen.ticketapp.interfaces.services.UserService;
import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.inject.spi.CDI;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

    // Méthode pour lire un fichier JSON
    private String readFromJsonFile(String filePath) throws IOException {
        // Utilise java.nio.file pour lire le fichier
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    @Override
    public List<UserModel> getUsers() {
        // Lecture du contenu du fichier JSON depuis le dossier resources
        String rawJSON;
        try {
            rawJSON = readFromJsonFile("src/main/resources/Utilisateur.json");
        } catch (IOException e) {
            throw new RuntimeException("Erreur de lecture du fichier JSON", e); // Gérer l'IOException
        }

        ObjectMapper objectMapper = new ObjectMapper();

        // Lecture de la liste de users
        try {
            return objectMapper.readValue(rawJSON, new TypeReference<List<UserModel>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erreur lors du parsing JSON", e); // Gérer l'exception JsonProcessingException
        }
    }


    @Override
    public UserModel getUserById(int id) {
        // Implémentez votre logique pour récupérer un utilisateur par ID
        String rawJSON;
        try {
            rawJSON = readFromJsonFile("src/main/resources/Utilisateur.json");
        } catch (IOException e) {
            throw new RuntimeException("Erreur de lecture du fichier JSON", e);
        }

        ObjectMapper objectMapper = new ObjectMapper();

        // Désérialiser le fichier JSON en liste d'utilisateurs
        List<UserModel> users;
        try {
            users = objectMapper.readValue(rawJSON, new TypeReference<List<UserModel>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erreur de désérialisation du JSON", e);
        }

        // Recherche de l'utilisateur par ID
        for (UserModel user : users) {
            if (user.id == id) {
                return user;  // Retourner l'utilisateur trouvé
            }
        }

        throw new RuntimeException("Utilisateur non trouvé pour l'ID " + id);  // Jette une exception si non trouvé
    }

    @Override
    public UserModel addUser(UserModel user) {
        return null;
    }

    @Override
    public UserModel removeUser(int id) {
        return null;
    }

    @Override
    public UserModel updateUser(UserModel user) {
        return null;
    }

    AgroalDataSource dataSource = CDI.current().select(AgroalDataSource.class).get();

    @Override
    public List<UserModel> getUsersBDD() {
        List<UserModel> users = new ArrayList<>(); // Initialisation de la liste
        try (Connection conn = dataSource.getConnection()) {
            // Préparation de la requête SQL
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id, nom, email, password, last_connexion, statut, role FROM user")) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        // Création d'un nouvel objet TicketModel
                        UserModel user = new UserModel();
                        user.id = rs.getInt("id");
                        user.nom = rs.getString("nom");
                        user.email = rs.getString("email");
                        user.password = rs.getString("password");
                        user.last_connexion = rs.getDate("last_connexion");
                        user.statut = rs.getBoolean("statut");
                        user.role = ROLE.valueOf(rs.getString("role"));

                        users.add(user);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des users : " + e.getMessage(), e);
        }

        return users; // Retourne la liste des tickets
    }

    @Override
    public UserModel getUserByIdBDD(int id) {
        UserModel user = new UserModel();
        try (Connection conn = dataSource.getConnection()) {
            // Ticket principal
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id, nom, email, password, last_connexion, statut, role FROM user WHERE id = ?")) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        user.id = rs.getInt("id");
                        user.nom = rs.getString("nom");
                        user.email = rs.getString("email");
                        user.password = rs.getString("password");
                        user.last_connexion = rs.getDate("last_connexion");
                        user.statut = rs.getBoolean("statut");
                        user.role = ROLE.valueOf(rs.getString("role"));
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du user : " + e.getMessage(), e);
        }

        return user;
    }

    @Override
    public void addUserBDD(UserModel user) {
        String query = "INSERT INTO user (nom, email, password, last_connexion, statut, role) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1,user.nom);
            stmt.setString(2,user.email);
            stmt.setString(3,user.password);
            stmt.setDate(4, new java.sql.Date(user.last_connexion.getTime()));
            stmt.setBoolean(5,user.statut);
            stmt.setString(6,user.role.toString());


            // Exécution de la requête
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Le user a été ajouté avec succès !");
            } else {
                System.out.println("Échec de l'ajout du user.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout du user : " + e.getMessage(), e);
        }
    }

    @Override
    public void updateUserBDD(UserModel user) {
        String query = "UPDATE user SET nom = ?, email = ?, password = ?, last_connexion = ?, statut = ?, role = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1,user.nom);
            stmt.setString(2,user.email);
            stmt.setString(3,user.password);
            stmt.setDate(4, new java.sql.Date(user.last_connexion.getTime()));
            stmt.setBoolean(5,user.statut);
            stmt.setString(6,user.role.toString());
            stmt.setInt(7,user.id);

            // Exécution de la requête
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Le user a été mis à jour avec succès !");
            } else {
                System.out.println("Échec de la mise à jour : aucun user trouvé avec l'ID " + user.id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du user : " + e.getMessage(), e);
        }
    }

    @Override
    public void removeUserBDD(int id) {
        String query = "DELETE FROM user WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Définir le paramètre ID
            stmt.setInt(1, id);

            // Exécuter la requête
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Le user avec l'ID " + id + " a été supprimé avec succès !");
            } else {
                System.out.println("Aucun user trouvé avec l'ID " + id + ".");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du user : " + e.getMessage(), e);
        }
    }
}
