package fr.isen.ticketapp.impl.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.isen.ticketapp.interfaces.models.UserModel;
import fr.isen.ticketapp.interfaces.services.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {

    private final String filePath = "src/main/resources/User.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<UserModel> readUsers() throws IOException {
        String rawJSON = Files.readString(Paths.get(filePath));
        return objectMapper.readValue(rawJSON, new TypeReference<>() {});
    }

    private void writeUsers(List<UserModel> users) throws IOException {
        String updatedJSON = objectMapper.writeValueAsString(users);
        Files.writeString(Paths.get(filePath), updatedJSON);
    }

    @Override
    public List<UserModel> getUsers() {
        try {
            return readUsers();
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture des utilisateurs", e);
        }
    }

    @Override
    public UserModel getUserById(int id) {
        String rawJSON;
        try {
            rawJSON = Files.readString(Paths.get("src/main/resources/User.json"));
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture du fichier JSON", e);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        List<UserModel> users;
        try {
            users = objectMapper.readValue(rawJSON, new TypeReference<List<UserModel>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erreur lors de la conversion du JSON", e);
        }

        // Recherche du user par ID
        return users.stream()
                .filter(user -> user.id == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("L'Utilisateur avec l'ID " + id + " est introuvable"));
    }


    @Override
    public UserModel addUser(UserModel user) {
        try {
            List<UserModel> users = readUsers();
            users.add(user);
            writeUsers(users);
            return user;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'ajout de l'utilisateur", e);
        }
    }

    @Override
    public UserModel removeUser(int id) {
        try {
            List<UserModel> users = readUsers();
            List<UserModel> updatedUsers = users.stream()
                    .filter(user -> user.id != id)
                    .collect(Collectors.toList());
            if (users.size() == updatedUsers.size()) {
                throw new RuntimeException("Aucun utilisateur trouvé avec l'ID : " + id);
            }
            writeUsers(updatedUsers);
            return null;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'utilisateur", e);
        }
    }

    @Override
    public UserModel updateUser(UserModel user) {
        try {
            List<UserModel> users = readUsers();
            boolean updated = false;
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).id == user.id) {
                    users.set(i, user);
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                throw new RuntimeException("Aucun utilisateur trouvé avec l'ID : " + user.id);
            }
            writeUsers(users);
            return user;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'utilisateur", e);
        }
    }
}
