/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jakarta.udbl.jakartamission.buisness;

import com.jakarta.udbl.jakartamission.entities.Utilisateur;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author LENOVO
 */
@Stateless
public class UtilisateurEntrepriseBean {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void ajouterUtilisateurEntreprise(String username, String email, String password, String description) {
        // Hachage du mot de passe
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        // Création de l'entité Utilisateur
        Utilisateur utilisateur = new Utilisateur(username, email, hashedPassword, description);
        // Persistance dans la base de données
        em.persist(utilisateur);
    }

    public List<Utilisateur> listerTousLesUtilisateurs() {
        return em.createQuery("SELECT u FROM Utilisateur u", Utilisateur.class).getResultList();
    }

    @Transactional
    public void supprimerUtilisateur(Long id) {
        Utilisateur utilisateur = em.find(Utilisateur.class, id);
        if (utilisateur != null) {
            em.remove(utilisateur);
        }
    }

    public Utilisateur trouverUtilisateurParId(Long id) {
        return em.find(Utilisateur.class, id);
    }

    public Utilisateur trouverUtilisateurParEmail(String email) {
        try {
            return em.createQuery("SELECT u FROM Utilisateur u WHERE u.email = :email", Utilisateur.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    // Méthode pour vérifier un mot de passe
    public boolean verifierMotDePasse(String password, String hashedPassword) { 
        return BCrypt.checkpw(password, hashedPassword); 
    } 
    public Utilisateur authentifier(String email, String password){
        Utilisateur utilisateur = this.trouverUtilisateurParEmail(email);
        if (utilisateur != null && this.verifierMotDePasse(password, utilisateur.getPassword())){
            return utilisateur;
            }
        return null;
        
    }

    @Transactional
    public void modifierUtilisateur(Utilisateur utilisateur, String newPassword) {
        if (newPassword != null && !newPassword.isEmpty()) {
            utilisateur.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        }
        em.merge(utilisateur);
    }
}