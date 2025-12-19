package com.jakarta.udbl.jakartamission.newpackage.beans;

import com.jakarta.udbl.jakartamission.buisness.SessionManager;
import com.jakarta.udbl.jakartamission.buisness.UtilisateurEntrepriseBean;
import com.jakarta.udbl.jakartamission.entities.Utilisateur;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * @author LENOVO
 */
@Named
@RequestScoped
public class UtilisateurBean implements Serializable {

    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Size(min = 3, max = 50, message = "Le nom d'utilisateur doit avoir entre 3 et 50 caractères")
    private String username;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$",
        message = "Le mot de passe doit contenir une majuscule, un chiffre et un caractère spécial"
    )
    private String password;

    @NotBlank(message = "Veuillez confirmer votre mot de passe")
    private String confirmPassword;

    private String description;

    private String currentPassword;
    private String newPassword;

    @EJB
    private UtilisateurEntrepriseBean utilisateurEntrepriseBean;

    @Inject
    private SessionManager sessionManager;

    private Utilisateur currentUtilisateur;

    @PostConstruct
    public void init() {
        String emailSession = sessionManager.getValueFromSession("user");
        if (emailSession != null) {
            currentUtilisateur = utilisateurEntrepriseBean.trouverUtilisateurParEmail(emailSession);
            if (currentUtilisateur != null) {
                this.username = currentUtilisateur.getUsername();
                this.email = currentUtilisateur.getEmail();
                this.description = currentUtilisateur.getDescription();
            }
        }
    }

    public String ajouterUtilisateur() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (!password.equals(confirmPassword)) {
            context.addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Les mots de passe ne correspondent pas", null));
            return null;
        }

        try {
            utilisateurEntrepriseBean
                .ajouterUtilisateurEntreprise(username, email, password, description);

            context.addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Utilisateur ajouté avec succès", null));

            // reset
            username = email = password = confirmPassword = description = "";

        } catch (Exception e) {
            context.addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Erreur lors de l'ajout : " + e.getMessage(), null));
        }

        return null;
    }

    public String modifierProfil() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (currentUtilisateur == null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Utilisateur non connecté", null));
                return null;
            }

            // Mettre à jour les informations de base
            currentUtilisateur.setUsername(this.username);
            
            String oldEmail = currentUtilisateur.getEmail();
            boolean emailChanged = !oldEmail.equals(this.email);
            currentUtilisateur.setEmail(this.email);
            
            currentUtilisateur.setDescription(this.description);

            // Gérer le changement de mot de passe si fourni
            if (newPassword != null && !newPassword.isEmpty()) {
                if (!newPassword.equals(confirmPassword)) {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Les nouveaux mots de passe ne correspondent pas", null));
                    return null;
                }
                utilisateurEntrepriseBean.modifierUtilisateur(currentUtilisateur, newPassword);
            } else {
                // Persister toutes les modifications (username, email, description)
                utilisateurEntrepriseBean.modifierUtilisateur(currentUtilisateur, null);
            }

            // Si l'email a changé, mettre à jour la session
            if (emailChanged) {
                sessionManager.createSession("user", this.email);
            }

            // Réinitialiser les champs de mot de passe après succès
            newPassword = "";
            confirmPassword = "";

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Profil mis à jour avec succès", null));
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur lors de la mise à jour : " + e.getMessage(), null));
        }
        return null;
    }

    public String deconnexion() {
        sessionManager.invalidateSession();
        return "/index.xhtml?faces-redirect=true";
    }

    // Getters & Setters CORRECTS
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCurrentPassword() { return currentPassword; }
    public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
