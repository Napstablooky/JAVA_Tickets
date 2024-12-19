package org.acme;

public class Ticket {
    private String id;
    private String titre;
    private String description;
    private String dateCreation;
    private String dateMiseAJour;
    private String impact;
    private String etat;
    private String utilisateurId;
    private String posteInformatiqueId;
    private String typeDemande;

    // Getters et Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitre() {
        return titre;
    }
    public void setTitre(String titre) {
        this.titre = titre;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDateCreation() {
        return dateCreation;
    }
    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }
    public String getDateMiseAJour() {
        return dateMiseAJour;
    }
    public void setDateMiseAJour(String dateMiseAJour) {
        this.dateMiseAJour = dateMiseAJour;
    }
    public String getImpact() {
        return impact;
    }
    public void setImpact(String impact) {
        this.impact = impact;
    }
    public String getEtat() {
        return etat;
    }
    public void setEtat(String etat) {
        this.etat = etat;
    }
    public String getUtilisateurId() {
        return utilisateurId;
    }
    public void setUtilisateurId(String utilisateurId) {
        this.utilisateurId = utilisateurId;
    }
    public String getPosteInformatiqueId() {
        return posteInformatiqueId;
    }
    public void setPosteInformatiqueId(String posteInformatiqueId) {
        this.posteInformatiqueId = posteInformatiqueId;
    }
    public String getTypeDemande() {
        return typeDemande;
    }
    public void setTypeDemande(String typeDemande) {
        this.typeDemande = typeDemande;
    }

    @Override
    public String toString() {
        return "ID: " + id + "\n" +
                "Titre: " + titre + "\n" +
                "Description: " + description + "\n" +
                "Date de création: " + dateCreation + "\n" +
                "Date de mise à jour: " + dateMiseAJour + "\n" +
                "Impact: " + impact + "\n" +
                "État: " + etat + "\n" +
                "Utilisateur ID: " + utilisateurId + "\n" +
                "Poste Informatique ID: " + posteInformatiqueId + "\n" +
                "Type de demande: " + typeDemande;
    }
}
