/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sio.leo.direction.des.sports.modele;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.time.LocalDate;
import sio.leo.direction.des.sports.modele.DAO;

/**
 *
 * @author baras
 */
public class CrudInscription {
    private static final Connection cnx=DAO.getConnection();
    
    private static final Statement smt =DAO.getStatement();
    
    private ResultSet rs =null;
    
    /*
    public void insererLardon(int id, Lardon lardon) throws SQLException{
        if(!idExist(id)){
            if(lardon.getDateNais() == null){
                String requete = "INSERT INTO lardon(id, prenom, nom, cheveux) values ("+id+",'"+lardon.getPrenom()+"', '"+lardon.getNom()+"', '"+lardon.getCheveux()+"');";
                //System.out.println(requete);
                smt.executeUpdate(requete);
                System.out.println("Insertion réussite (sans date de naissance!");
            }
            else{
                String requete = "INSERT INTO lardon(id, prenom, nom, cheveux, ddn) values ("+id+",'"+lardon.getPrenom()+"', '"+lardon.getNom()+"', '"+lardon.getCheveux()+"', '"+lardon.getDateNais()+"');";
                //System.out.println(requete);
                smt.executeUpdate(requete);
                System.out.println("Insertion réussite !");
            }
            
        }
        else
            System.out.println("Impossible d'ajouter ce lardon dans la table car son id existe déjà !");
    }
    */
    
    public String insertUser(String id, String mdp, String nom, String prenom, String cp, LocalDate date, String num, String qst) throws IOException, SQLException, SQLIntegrityConstraintViolationException, Exception{
        try{
            if(!id.isEmpty() && !mdp.isEmpty() && !nom.isEmpty() && !num.isEmpty() && !qst.isEmpty() && !cp.isEmpty())
                if(!idExist(id)){
                    int cpInt = Integer.parseInt(cp);
                    String originalData = mdp;
                    String encryptedMdp = Encryptor.encrypt(originalData);
                    String requete = "INSERT INTO utilisateur(id, mdp, nom, prenom, cp, ddn, num, qst) values ("+id+",'"+encryptedMdp+"','"+nom+"', '"+prenom+"', '"+cpInt+"','"+date+"', '"+num+"', '"+qst+"');";
                    smt.executeUpdate(requete);
                    return "Inscription réussie";
                }
            else{
                return "Un compte existe déjà à cet identifiant";
            }
        }catch(NumberFormatException e)
        {
            return "Votre saisi doit être compléte et/ou dans le bon format";
        }  
        return "Votre saisi doit être compléte et/ou dans le bon format";
    }
    
    public Boolean getMdp(String id, String mdp) throws IOException, SQLException, SQLIntegrityConstraintViolationException, Exception{
        try{
            if(!id.isEmpty() && !mdp.isEmpty()){
                if(idExist(id)){
                    String originalData = mdp;
                    String encryptedMdp = Encryptor.encrypt(originalData);
                    String requete = "SELECT mdp FROM utilisateur WHERE id = ? AND mdp = ?";
                    PreparedStatement preparedStatement = cnx.prepareStatement(requete, ResultSet.TYPE_SCROLL_INSENSITIVE);
                    preparedStatement.setString(1, id);
                    preparedStatement.setString(2, encryptedMdp);
                    //System.out.println(mdp);
                    //System.out.println(encryptedMdp);
                    this.rs = preparedStatement.executeQuery();
                    while(rs.next()){
                        String mdpCrypt  = rs.getString("mdp");
                        //System.out.println(mdpCrypt);
                        if(encryptedMdp.equals(mdpCrypt)){
                            return true;
                             }
                        }
                        return false; // Si la boucle ne trouve pas de correspondance
                    } 
                else {
                    return false; // Si id n'existe pas
                }
            } 
            else {
                return false; // Si id ou mdp sont vides
            }
        } catch (NumberFormatException e) {
            return false; // Gestion spécifique si une exception de format numérique se produit
        }
    }
    
    /*
    public void updateLardonCheveux(int id, String nom) throws SQLException{
        if(idExist(id)){
            String requete = "UPDATE lardon SET cheveux ='"+nom+"' WHERE id ="+id+";";
            
            smt.execute(requete);
            System.out.println("Modification réussie de l'id : " + id );
        }
        else{
            System.out.println("L'id : " + id + " n'existe pas");
        }
    }
*/
    
    public Boolean idExist(String id) throws SQLException, SQLIntegrityConstraintViolationException{
        String requete = "SELECT id FROM utilisateur WHERE id ="+id+";";
        this.rs = smt.executeQuery(requete);
        while(rs.next()){
            String idAll  = rs.getString("id");
            if(idAll.equals(id)){
                return true;
            }
        }   
        return false;
    }
    
    public static String convertDate(LocalDate d){
        //transforme la date format mariadb (aaaa-mm-jj) par aaaa/mm/jj 
        if(d != null){
            String dateFormatReplace = d.toString();
            String dateFormat = dateFormatReplace.replace('/', '-');
            return dateFormat;
        }
        return null;
        // pour instancier un lardon
    }
}