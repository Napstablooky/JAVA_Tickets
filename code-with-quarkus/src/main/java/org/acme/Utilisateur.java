package org.acme;

public class Utilisateur {
    private String id;
    private String nom;
    private String email;
    private String motDePasse;
    private String dateDerniereConnexion;
    private String statut; // "actif" ou "inactif"
    private String role;   // "utilisateur" ou "intervenant"

    // Getters et setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getMotDePasse() {
        return motDePasse;
    }
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
    public String getDateDerniereConnexion() {
        return dateDerniereConnexion;
    }
    public void setDateDerniereConnexion(String dateDerniereConnexion) {
        this.dateDerniereConnexion = dateDerniereConnexion;
    }
    public String getStatut() {
        return statut;
    }
    public void setStatut(String statut) {
        this.statut = statut;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
