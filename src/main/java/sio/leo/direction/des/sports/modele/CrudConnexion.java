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
import sio.leo.direction.des.sports.Utilisateur;

/**
 *
 * @author CAYUELA
 */
public class CrudConnexion {
    //Initialisation de la connexion au DAO
  private static final Connection cnx=DAO.getConnection();
    
    private static final Statement smt =DAO.getStatement();
    
    //Déclaration du resultSet
    private ResultSet rs =null;
    
    
    /**
     * Méthode qui vérifie si l'id et le mdp sont identiques à la BDD
     * @param id
     * @param mdp
     * @return
     * @throws IOException
     * @throws SQLException
     * @throws SQLIntegrityConstraintViolationException
     * @throws Exception 
     */
     public Boolean getMdp(String id, String mdp) throws IOException, SQLException, SQLIntegrityConstraintViolationException, Exception{ 
        try{
            if(!id.isEmpty() && !mdp.isEmpty()){
                if(idExist(id)){
                    String originalData = mdp;
                    String encryptedMdp = Encryptor.encrypt(originalData);
                    String requete = "SELECT UTI_PASSWORD FROM UTILISATEUR WHERE UTI_ID = ? AND UTI_PASSWORD = ?"; //Selectione le mdp et l'id dans la base
                    PreparedStatement preparedStatement = cnx.prepareStatement(requete, ResultSet.TYPE_SCROLL_INSENSITIVE); // execute la requête ci-dessus
                    preparedStatement.setString(1, id);
                    preparedStatement.setString(2, encryptedMdp);
                    //System.out.println(mdp);
                    //System.out.println(encryptedMdp);
                    this.rs = preparedStatement.executeQuery();
                    while(rs.next()){
                        String mdpCrypt  = rs.getString("UTI_PASSWORD"); //Crypte le mdp et vérifie s'il est le même que celui crypter dans la BDD
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
     
     /**
      * Méthode qui instancie un utilisateur avec les informations de la BBD lors de la connexion
      * @param id
      * @return 
      */
    public Utilisateur requeteUtilisateur(String id) 
    {
        Utilisateur leClient;
        try
        {
            
            String query = "Select * from UTILISATEUR where UTI_ID = ?;";
            PreparedStatement preparedStatement = cnx.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE);
            preparedStatement.setString(1, id);   
            this.rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                String nom=rs.getString("UTI_NOM");
                String prenom=rs.getString("UTI_PRENOM");
                java.sql.Date sqlDate = rs.getDate("UTI_DDN");
                LocalDate ddn = sqlDate.toLocalDate();
                //LocalDate ddn=(LocalDate)rs.getObject("UTI_DDN");
                
                int categorie = rs.getInt("CAT_CODE");
                String telephone =rs.getString("UTI_TELEPHONE"); 
                
                leClient = new Utilisateur(id, nom, prenom, ddn, categorie, telephone); //Crée un nouvel utilisateur lors de la connexion
                return leClient;
            }
            
        }
        catch(SQLException e)
        {
            System.out.println("Erreur "+e);
        }

        return null;
        
    }
        
       /**
        * Méthode qui vérifie si l'id existe dans la BDD
        * @param id
        * @return
        * @throws SQLException
        * @throws SQLIntegrityConstraintViolationException 
        */
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
    
}
