/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jakarta.udbl.jakartamission.newpackage.beans;

import com.jakarta.udbl.jakartamission.buisness.SessionManager;
import com.jakarta.udbl.jakartamission.buisness.UtilisateurEntrepriseBean;
import com.jakarta.udbl.jakartamission.entities.Utilisateur;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.faces.context.FacesContext;
import jakarta.faces.application.FacesMessage;
import jakarta.inject.Inject;
import java.io.Serializable;

/**
 *
 * @author LENOVO
 */
@Named(value = "welcomeBean")
@RequestScoped
public class welcomeBean implements Serializable {
    
    // Injection de l'EJB UtilisateurEntrepriseBean
    @EJB
    private UtilisateurEntrepriseBean utilisateurEntrepriseBean;
    
    @Inject
    private transient SessionManager sessionManager;
    
    // Propriétés pour le formulaire
    private String username;
    private String email;
    private String password;
    private String description;
    
    // Message de bienvenue ou de résultat
    private String message;
    
    /**
     * Creates a new instance of welcomeBean
     */
    public welcomeBean() {
    }
    
    // Getters et Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    // Méthode pour ajouter un utilisateur
    public String ajouterUtilisateur() {
        try {
            utilisateurEntrepriseBean.ajouterUtilisateurEntreprise(username, email, password, description);
            message = "Utilisateur ajouté avec succès !";
            // Réinitialiser les champs
            username = null;
            email = null;
            password = null;
            description = null;
            return "success";
        } catch (Exception e) {
            message = "Erreur lors de l'ajout de l'utilisateur : " + e.getMessage();
            return "error";
        }
    }
    
    // Méthode pour authentifier un utilisateur
    public String sAuthentifier(){
              Utilisateur utilisateur = utilisateurEntrepriseBean.authentifier(email,password);
              FacesContext context = FacesContext.getCurrentInstance();
              if(utilisateur != null){
                sessionManager.createSession("user", email);
                  return "home?faces-redirect=true";
              }
              else{
                  this.message = "Email ou mot de passe incorrecte.! ";
                  context.addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,message,null));
                  return null;
              }
    }
}
