package fr.isen.ticketapp.interfaces.services;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.isen.ticketapp.interfaces.models.PosteInfoModel;
import fr.isen.ticketapp.interfaces.models.TicketModel;

public interface DeviceService {
    List<PosteInfoModel> getDevices() throws JsonProcessingException;

    PosteInfoModel getDeviceById(final int id) throws JsonProcessingException;

    PosteInfoModel addOneDevice(final PosteInfoModel device);

    PosteInfoModel removeOneDevice(final int id);

    Integer updateOneDevice(final PosteInfoModel device);

    List<PosteInfoModel> getDevicesBDD();

    PosteInfoModel getDeviceByIdBDD(int id);

    void addDeviceBDD(PosteInfoModel device);

    void updateDeviceBDD(PosteInfoModel device);

    void removeDeviceBDD(int id);
}
