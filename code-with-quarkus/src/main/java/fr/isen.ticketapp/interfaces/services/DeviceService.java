package fr.isen.ticketapp.interfaces.services;

import java.util.List;
import fr.isen.ticketapp.interfaces.models.PosteInfoModel;

public interface DeviceService {
    List<PosteInfoModel> getDevices();

    PosteInfoModel getDeviceById(final int id);

    PosteInfoModel addDevice(final PosteInfoModel device);

    PosteInfoModel removeDevice(final int id);

    PosteInfoModel updateDevice(final PosteInfoModel device);

}
