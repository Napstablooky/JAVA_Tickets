package fr.isen.ticketapp.interfaces.models;

import java.util.Date;
import fr.isen.ticketapp.interfaces.models.enums.ROLE;

public class UserModel {
    public int id;

    public String nom;

    public String email;

    public String password;

    public Date last_connexion;

    public boolean statut;

    public ROLE role;

}
