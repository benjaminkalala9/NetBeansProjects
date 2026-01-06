package com.jakarta.udbl.jakartamission.buisness;

import com.jakarta.udbl.jakartamission.entities.Visite;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@Stateless
@LocalBean
public class VisiteEntrepriseBean {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void ajouterVisite(Visite visite) {
        em.persist(visite);
    }

    public List<Visite> listerToutesLesVisites() {
        return em.createQuery("SELECT v FROM Visite v", Visite.class).getResultList();
    }

    public List<Visite> listerVisitesParUtilisateur(com.jakarta.udbl.jakartamission.entities.Utilisateur utilisateur) {
        return em.createQuery("SELECT v FROM Visite v WHERE v.utilisateur = :user", Visite.class)
                 .setParameter("user", utilisateur)
                 .getResultList();
    }

    @Transactional
    public void supprimerVisite(int id) {
        Visite v = em.find(Visite.class, id);
        if (v != null) {
            em.remove(v);
        }
    }

    @Transactional
    public void modifierVisite(Visite visite) {
        em.merge(visite);
    }

    public Visite trouverVisiteParId(int id) {
        return em.find(Visite.class, id);
    }
}
