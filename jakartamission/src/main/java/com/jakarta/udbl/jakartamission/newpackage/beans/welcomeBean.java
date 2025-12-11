/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jakarta.udbl.jakartamission.newpackage.beans;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

/**
 *
 * @author LENOVO
 */
@RequestScoped
@Named
public class welcomeBean {
    private String nom;
    private String message;
    // Pour le convertisseur de devises
    private Double usd;
    private Double idr;
    private Double idr2;
    private Double usd2;
    private final double TAUX_USD_IDR = 15500.0; // taux fixe exemple
   public String getNom(){
       return nom;
   }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMessage() {
        return message;
    }
   
   public void afficher(){
       this.message= "Welcome to Indonesia dear "+this.nom;
   }

    // Getters et setters pour le convertisseur
    public Double getUsd() { return usd; }
    public void setUsd(Double usd) { this.usd = usd; }
    public Double getIdr() { return idr; }
    public void setIdr(Double idr) { this.idr = idr; }
    public Double getIdr2() { return idr2; }
    public void setIdr2(Double idr2) { this.idr2 = idr2; }
    public Double getUsd2() { return usd2; }
    public void setUsd2(Double usd2) { this.usd2 = usd2; }

    // Conversion USD -> IDR
    public void convertirEnIdr() {
        if (usd != null) {
            idr = usd * TAUX_USD_IDR;
        } else {
            idr = null;
        }
    }

    // Conversion IDR -> USD
    public void convertirEnUsd() {
        if (idr2 != null) {
            usd2 = idr2 / TAUX_USD_IDR;
        } else {
            usd2 = null;
        }
    }
}
