package fr.isen.ticketapp.impl.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.isen.ticketapp.interfaces.models.enums.ROLE;
import fr.isen.ticketapp.interfaces.models.UserModel;
import fr.isen.ticketapp.interfaces.services.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class UserServiceImpl implements UserService {

    // Méthode pour lire un fichier JSON
    private String readFromJsonFile(String filePath) throws IOException {
        // Utilise java.nio.file pour lire le fichier
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    @Override
    public String getUsers() {
        // Lecture du contenu du fichier JSON depuis le dossier resources
        String rawJSON = null;
        try {
            rawJSON = readFromJsonFile("src/main/resources/Utilisateur.json");
        } catch (IOException e) {
            throw new RuntimeException("Erreur de lecture du fichier JSON", e); // Gérer l'IOException
        }

        ObjectMapper objectMapper = new ObjectMapper();

        // Lecture de la liste de users
        List<UserModel> users = null;
        try {
            users = objectMapper.readValue(rawJSON, new TypeReference<List<UserModel>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erreur lors du parsing JSON", e); // Gérer l'exception JsonProcessingException
        }

        // Convertir la liste des users en une chaîne JSON
        try {
            return objectMapper.writeValueAsString(users); // Convertir la liste en chaîne JSON
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erreur lors de la conversion des utilisateurs en JSON", e);
        }
    }


    @Override
    public UserModel getUserById(int id) {
        // Implémentez votre logique pour récupérer un utilisateur par ID
        String rawJSON = null;
        try {
            rawJSON = readFromJsonFile("src/main/resources/Utilisateur.json");
        } catch (IOException e) {
            throw new RuntimeException("Erreur de lecture du fichier JSON", e);
        }

        ObjectMapper objectMapper = new ObjectMapper();

        // Désérialiser le fichier JSON en liste d'utilisateurs
        List<UserModel> users = null;
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
}
