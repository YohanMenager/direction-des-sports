/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sio.leo.direction.des.sports;

import java.time.LocalDate;

/**
 *
 * @author MENAGER
 */
class Utilisateur {
    private String id;
    private String nom;
    private String prenom;
    private LocalDate ddn;
    private int categorie;

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public LocalDate getDdn() {
        return ddn;
    }

    public int getCategorie() {
        return categorie;
    }
    

    public void setId(String id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setDdn(LocalDate ddn) {
        this.ddn = ddn;
    }

    public void setCategorie(int categorie) {
        this.categorie = categorie;
    }
    
        
}
