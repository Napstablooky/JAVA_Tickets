package fr.isen.ticketapp.interfaces.models;

import java.util.Date;
import fr.isen.ticketapp.interfaces.models.enums.ETATTICKET;
import fr.isen.ticketapp.interfaces.models.enums.IMPACT;

public class TicketModel {
    public int id;

    public IMPACT impact;

    public String titre;

    public String description;

    public Date date_crea;

    public ETATTICKET etat;

    public Date date_maj;

    public UserModel createur;

    public PosteInfoModel poste_info;

    public String type_demande;
}
