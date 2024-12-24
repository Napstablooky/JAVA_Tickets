package fr.isen.ticketapp.interfaces.services;

import java.util.List;
import fr.isen.ticketapp.interfaces.models.UserModel;

public interface UserService {
    List<UserModel> getUsers();

    UserModel getUserById(final int id);

    UserModel addUser(final UserModel user);

    UserModel removeUser(final int id);

    UserModel updateUser(final UserModel user);

    List<UserModel> getUsersBDD();

    UserModel getUserByIdBDD(int id);

    void addUserBDD(UserModel user);

    void updateUserBDD(UserModel user);

    void removeUserBDD(int id);
}
