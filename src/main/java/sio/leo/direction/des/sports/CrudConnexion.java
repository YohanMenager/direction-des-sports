/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sio.leo.direction.des.sports;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.time.LocalDate;
import sio.leo.direction.des.sports.DAO;
import sio.leo.direction.des.sports.Utilisateur;
import java.time.LocalDate;
import sio.leo.direction.des.sports.App;
import sio.leo.direction.des.sports.DAO;
import sio.leo.direction.des.sports.Utilisateur;

/**
 *
 * @author CAYUELA
 */
public class CrudConnexion {
  private static final Connection cnx=DAO.getConnection();
    
    private static final Statement smt =DAO.getStatement();
    
    private ResultSet rs =null;
    
     public Boolean getMdp(String id, String mdp) throws IOException, SQLException, SQLIntegrityConstraintViolationException, Exception{
        try{
            if(!id.isEmpty() && !mdp.isEmpty()){
                if(idExist(id)){
                    String originalData = mdp;
                    String encryptedMdp = Encryptor.encrypt(originalData);
                    String requete = "SELECT UTI_PASSWORD FROM UTILISATEUR WHERE UTI_ID = ? AND UTI_PASSWORD = ?";
                    try{PreparedStatement preparedStatement = cnx.prepareStatement(requete, ResultSet.TYPE_SCROLL_INSENSITIVE);
                        {
                            preparedStatement.setString(1, "'"+id+"'");
                            preparedStatement.setString(2, "'"+encryptedMdp+"'");
                            this.rs = preparedStatement.executeQuery();                      
                        }
                        preparedStatement.close();
                    
                    
                    }
                        catch(SQLException e)
                        {
                            System.out.println("erreur : "+e);
                        }
                    //System.out.println(mdp);
                    //System.out.println(encryptedMdp);
                    while(rs.next()){
                    System.out.println("\n TEST \n");
                        String mdpCrypt  = rs.getString("UTI_MDP");
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

    public Utilisateur requeteUtilisateur(String id)
    {
        Utilisateur leClient;
        try
        {
            
            String query = "Select * from UTILISATEUR where UTI_ID = '?';";
            PreparedStatement preparedStatement = cnx.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE);
            preparedStatement.setString(1, id);   
            this.rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                String nom=rs.getString("UTI_NOM");
                String prenom=rs.getString("UTI_PRENOM");
                LocalDate ddn=(LocalDate)rs.getObject("UTI_DDN");
                int categorie = rs.getInt("CAT_CODE");
                String telephone =rs.getString("UTI_TELEPHONE"); 
                
                leClient = new Utilisateur(id, nom, prenom, ddn, categorie, telephone);
                return leClient;
            }
            
        }
        catch(SQLException e)
        {
            System.out.println("Erreur "+e);
        }

        return null;
        
    }
        
       
}