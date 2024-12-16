package fr.isen.ticketapp.interfaces.models;

import fr.isen.ticketapp.interfaces.models.enums.ETATPOSTE;

public class PosteInfoModel {
    public int id;

    public UserModel user;

    public ETATPOSTE etat;

    public ConfigurationModel configuration;

}
