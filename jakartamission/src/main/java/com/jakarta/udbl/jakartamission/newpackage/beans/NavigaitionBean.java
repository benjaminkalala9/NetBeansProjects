/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jakarta.udbl.jakartamission.newpackage.beans;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.IOException;

/**
 *
 * @author LENOVO
 */
@Named(value = "navigationController")
@RequestScoped
public class NavigaitionBean {
    public void voirApropos(){
        try{
            String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/pages/a_propos.xhtml");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void voirLieu(){
        try{
            String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/pages/lieu.xhtml");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void voirHome(){
        try{
            String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/index.xhtml");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
