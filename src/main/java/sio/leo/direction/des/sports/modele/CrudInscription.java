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
import java.util.ArrayList;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import sio.leo.direction.des.sports.modele.DAO;

/**
 *
 * @author baras
 */
public class CrudInscription {
    private static final Connection cnx=DAO.getConnection();
    
    private static final Statement smt =DAO.getStatement();
    
    private ResultSet rs =null;
    
    public String insertUser(String id, String mdp, String nom, String prenom, String cp, LocalDate date, String num, String qst, int idqst) throws IOException, SQLException, SQLIntegrityConstraintViolationException, Exception{
        try{
            if(!id.isEmpty() && !mdp.isEmpty() && !nom.isEmpty() && !num.isEmpty() && !qst.isEmpty() && !cp.isEmpty())
                if(!idExist(id)){
                    LocalDate currentDate = LocalDate.now();
                    int yearsDifference = currentDate.getYear() - date.getYear();
                    int categorie = 0;
                    if(yearsDifference<12)
                        categorie=4;
                    else if(yearsDifference<25)
                        categorie = 1;
                    else if(yearsDifference>65)
                        categorie = 2;
                    int cpInt = Integer.parseInt(cp);
                    String originalData = mdp;
                    String encryptedMdp = Encryptor.encrypt(originalData);
                    String requete = "INSERT INTO UTILISATEUR value ('"+id+"','"+nom+"','"+prenom+"','"+num+"','"+date+"', '"+categorie+"', '"+encryptedMdp+"', '"+qst+"', '"+cpInt+"', '"+idqst+"');";
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
                    String requete = "SELECT UTI_PASSWORD FROM UTILISATEUR WHERE UTI_ID = ? AND UTI_MDP = ?";
                    PreparedStatement preparedStatement = cnx.prepareStatement(requete, ResultSet.TYPE_SCROLL_INSENSITIVE);
                    preparedStatement.setString(1, id);
                    preparedStatement.setString(2, encryptedMdp);
                    //System.out.println(mdp);
                    //System.out.println(encryptedMdp);
                    this.rs = preparedStatement.executeQuery();
                    while(rs.next()){
                        String mdpCrypt  = rs.getString("UTI_PASSWORD");
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
    
    
    public ArrayList<String> getQuestionSecrete() throws SQLException{
        
        ArrayList<String> options = new ArrayList<>();
        String query = "SELECT question FROM QUESTION_SECRETE ORDER BY id";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query);) {
            this.rs = preparedStatement.executeQuery();
            while (this.rs.next()) {
                // Ajouter chaque élément à la liste
                options.add(rs.getString("question"));
            }
        }
        return options;
    }
    
    public Boolean idExist(String id) throws SQLException, SQLIntegrityConstraintViolationException{
        String requete = "SELECT UTI_ID FROM UTILISATEUR WHERE UTI_ID ='"+id+"';";
        this.rs = smt.executeQuery(requete);
        while(rs.next()){
            String idAll  = rs.getString("UTI_ID");
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