package com.jakarta.udbl.jakartamission.newpackage.beans;

import com.jakarta.udbl.jakartamission.buisness.SessionManager;
import com.jakarta.udbl.jakartamission.buisness.VisiteEntrepriseBean;
import com.jakarta.udbl.jakartamission.buisness.LieuEntrepriseBean;
import com.jakarta.udbl.jakartamission.buisness.UtilisateurEntrepriseBean;
import com.jakarta.udbl.jakartamission.entities.Visite;
import com.jakarta.udbl.jakartamission.entities.Utilisateur;
import com.jakarta.udbl.jakartamission.entities.Lieu;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Named(value = "visiteBean")
@RequestScoped
public class VisiteBean implements Serializable {
    @Inject
    private VisiteEntrepriseBean visiteEntrepriseBean;

    @Inject
    private LieuEntrepriseBean lieuEntrepriseBean;

    @Inject
    private UtilisateurEntrepriseBean utilisateurEntrepriseBean;

    @Inject
    private SessionManager sessionManager;

    private Integer editingId = null;

    // UI fields bound to the view
    private LocalDate dateVisite;
    private Integer tempsPasse;
    private String observations;
    private Integer depense;
    private Integer lieuId;

    /** Returns the list of all visits for display in the UI. */
    public List<Visite> getVisites() {
        return visiteEntrepriseBean.listerToutesLesVisites();
    }

    /** Returns the list of visits for the connected user. */
    public List<Visite> getMesVisites() {
        String email = sessionManager.getValueFromSession("user");
        if (email != null) {
            Utilisateur user = utilisateurEntrepriseBean.trouverUtilisateurParEmail(email);
            if (user != null) {
                return visiteEntrepriseBean.listerVisitesParUtilisateur(user);
            }
        }
        return java.util.Collections.emptyList();
    }

    /** Returns the list of distinct places visited by the connected user. */
    public List<Lieu> getMesLieuxVisites() {
        List<Visite> visites = getMesVisites();
        return visites.stream()
                      .map(Visite::getLieu)
                      .distinct()
                      .collect(java.util.stream.Collectors.toList());
    }

    /** Creates a new Visite based on UI input. */
    public void ajouterVisite() {
        String email = sessionManager.getValueFromSession("user");
        Utilisateur user = utilisateurEntrepriseBean.trouverUtilisateurParEmail(email);
        Lieu lieu = lieuEntrepriseBean.trouverLieuParId(lieuId);
        Visite v = new Visite(dateVisite, tempsPasse, observations, depense, user, lieu);
        visiteEntrepriseBean.ajouterVisite(v);
        resetForm();
    }

    /** Prepares the bean for editing an existing Visite. */
    public void preparerModification(Visite v) {
        this.editingId = v.getId();
        this.dateVisite = v.getDateVisite();
        this.tempsPasse = v.getTempsPasse();
        this.observations = v.getObservations();
        this.depense = v.getDepense();
        this.lieuId = v.getLieu().getId();
    }

    /** Persists modifications made to the selected Visite. */
    public void enregistrerModification() {
        if (editingId != null) {
            Visite v = visiteEntrepriseBean.trouverVisiteParId(editingId);
            v.setDateVisite(dateVisite);
            v.setTempsPasse(tempsPasse);
            v.setObservations(observations);
            v.setDepense(depense);
            v.setLieu(lieuEntrepriseBean.trouverLieuParId(lieuId));
            visiteEntrepriseBean.modifierVisite(v);
            editingId = null;
            resetForm();
        }
    }

    /** Deletes a Visite identified by its id. */
    public void supprimerVisite(int id) {
        visiteEntrepriseBean.supprimerVisite(id);
    }

    /** Clears the UI fields after an add or edit operation. */
    private void resetForm() {
        dateVisite = null;
        tempsPasse = null;
        observations = null;
        depense = null;
        lieuId = null;
    }

    // Getters / Setters for UI binding
    public LocalDate getDateVisite() { return dateVisite; }
    public void setDateVisite(LocalDate dateVisite) { this.dateVisite = dateVisite; }
    public Integer getTempsPasse() { return tempsPasse; }
    public void setTempsPasse(Integer tempsPasse) { this.tempsPasse = tempsPasse; }
    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
    public Integer getDepense() { return depense; }
    public void setDepense(Integer depense) { this.depense = depense; }
    public Integer getLieuId() { return lieuId; }
    public void setLieuId(Integer lieuId) { this.lieuId = lieuId; }
}
