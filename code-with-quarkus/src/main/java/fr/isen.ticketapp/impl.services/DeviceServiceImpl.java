package fr.isen.ticketapp.impl.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.isen.ticketapp.interfaces.models.PosteInfoModel;
import fr.isen.ticketapp.interfaces.models.TicketModel;
import fr.isen.ticketapp.interfaces.services.DeviceService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
                return device; // Ticket trouvé
            }
        }

        throw new RuntimeException("Ticket avec l'ID " + id + " introuvable.");
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
}
