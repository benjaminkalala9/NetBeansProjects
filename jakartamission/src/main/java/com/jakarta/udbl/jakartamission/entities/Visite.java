package com.jakarta.udbl.jakartamission.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "visite")
public class Visite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date_visite")
    private LocalDate dateVisite;

    @Column(name = "temps_passe")
    private Integer tempsPasse;

    @Column(name = "observations", length = 1000)
    private String observations;

    @Column(name = "depense")
    private Integer depense;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "lieu_id")
    private Lieu lieu;

    public Visite() {}

    public Visite(LocalDate dateVisite, Integer tempsPasse, String observations, Integer depense, Utilisateur utilisateur, Lieu lieu) {
        this.dateVisite = dateVisite;
        this.tempsPasse = tempsPasse;
        this.observations = observations;
        this.depense = depense;
        this.utilisateur = utilisateur;
        this.lieu = lieu;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDate getDateVisite() { return dateVisite; }
    public void setDateVisite(LocalDate dateVisite) { this.dateVisite = dateVisite; }
    public Integer getTempsPasse() { return tempsPasse; }
    public void setTempsPasse(Integer tempsPasse) { this.tempsPasse = tempsPasse; }
    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
    public Integer getDepense() { return depense; }
    public void setDepense(Integer depense) { this.depense = depense; }
    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }
    public Lieu getLieu() { return lieu; }
    public void setLieu(Lieu lieu) { this.lieu = lieu; }
}
