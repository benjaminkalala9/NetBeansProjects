package com.jakarta.udbl.jakartamission;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import com.jakarta.udbl.jakartamission.buisness.LieuEntrepriseBean;
import com.jakarta.udbl.jakartamission.entities.Lieu;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.event.AjaxBehaviorEvent;
/**
 *
 * @author LENOVO
 * 
 */import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named(value = "lieuBean")
@RequestScoped
public class LieuBean implements Serializable{

    private String nom;
    private String description;
    private double longitude;
    private double latitude;
    private List<Lieu> lieux = new ArrayList<>();
    private Integer editingId = null;

    @Inject
    private LieuEntrepriseBean lieuEntrepriseBean;
    private String weatherMessage;
    private Integer selectedLieu;

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public List<Lieu> getLieux() { return lieuEntrepriseBean.listerTousLesLieux(); }

    public void ajouterLieu() {
        if (editingId != null) {
            // enregistrer modification
            lieuEntrepriseBean.modifierLieu(editingId, nom, description, latitude, longitude);
            annulerModification();
        } else {
            if (nom != null && !nom.isEmpty() && description != null && !description.isEmpty()) {
                lieuEntrepriseBean.ajouterLieuEntreprise(nom, description, latitude, longitude);
                // clear
                nom = "";
                description = "";
                latitude = 0;
                longitude = 0;
            }
        }
    }

    public void preparerModification(Lieu l) {
        if (l != null) {
            this.editingId = l.getId();
            this.nom = l.getNom();
            this.description = l.getDescription();
            this.latitude = l.getLatitude();
            this.longitude = l.getLongitude();
        }
    }

    public void annulerModification() {
        this.editingId = null;
        this.nom = "";
        this.description = "";
        this.latitude = 0;
        this.longitude = 0;
    }

    public void supprimerLieu(int id) {
        lieuEntrepriseBean.supprimerLieu(id);
    }

    public Integer getEditingId() { return editingId; }
    public void setEditingId(Integer editingId) { this.editingId = editingId; }
    
    public void fetchWeatherMessage(Lieu l) {
      
        if (selectedLieu != null) {
            // Appel au service web pour obtenir les données météorologiques
        
            String serviceURL = "http://localhost:8080/j-weather/webapi/JakartaWeather?latitude="
                    + l.getLatitude() + "&longitude=" + l.getLongitude();
            Client client = ClientBuilder.newClient();
            String response = client.target(serviceURL)
                    .request(MediaType.TEXT_PLAIN)
                    .get(String.class);
            // Enregistrement du message météo dans la variable weatherMessage
          this.weatherMessage =response;
        }
       
    }
    
    public void updateWeatherMessage(AjaxBehaviorEvent event) {
        
        Lieu lieu=lieuEntrepriseBean.trouverLieuParId(selectedLieu);
        this.fetchWeatherMessage(lieu);
    }
 public String getWeatherMessage() {
        return weatherMessage;
    }
    public void setWeatherMessage(String weatherMessage) {
        this.weatherMessage = weatherMessage;
    }

    public Integer getSelectedLieu() {
        return selectedLieu;
    }

    public void setSelectedLieu(Integer selectedLieu) {
        this.selectedLieu = selectedLieu;
    }

    public String visiterLieu() {
        if (selectedLieu != null) {
            return "/pages/ajouter_visite?faces-redirect=true&lieuId=" + selectedLieu;
        }
        return null;
    }
    
    
}
