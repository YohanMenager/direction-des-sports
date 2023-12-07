/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sio.leo.direction.des.sports;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import sio.leo.direction.des.sports.modele.DAO;
import sio.leo.direction.des.sports.modele.Encryptor;

/**
 * FXML Controller class
 *
 * @author MENAGER
 */
public class MdpOubliController implements Initializable {

    
    //------------------------------création des propriétés pour le FXML------------------------------//
    //labels
    @FXML
    private Label QuestionSecrete;
    
    @FXML
    private Label nouveauMdp;
    
    @FXML 
    private Label ConfirmerMdp;
    
    @FXML
    private Label Erreur;
        
    //textfields
    @FXML
    private TextField reponseQuestionSecrete;
    
    @FXML
    private TextField identifiant;
    
    //champs de mot de passe
    @FXML
    private PasswordField entreeNouveauMdp;
    
    @FXML
    private PasswordField entreeConfirmerMdp;
    
    //boutons
    @FXML
    private Button validerQuestion;
    
    @FXML
    private Button validerNvMdp;
    
    @FXML
    private Button validerId;
    
    
    //------------------------------création des propriétés pour les fonctions------------------------------//
    private String reponseQuestion;//valeur de la réponse attendue à la question secrète
    private String nvMdp;//nouveau mot de passe choisi par l'utilisateur
    private ResultSet rs;//pour récupérer des données dans la base de données
    private String id;//identifiant de l'utilisateur
    private PreparedStatement pstmt;//pour faire des prepared statement pour les requêtes
    
    Connection cnx = DAO.getConnection();//connexion à la base de données
    
    
    /**
     * initialisation
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //la plupart de la vue sera invisible tant qu'il n'y a pas besoin de la voir
        entreeNouveauMdp.setOpacity(0);
        entreeConfirmerMdp.setOpacity(0);
        nouveauMdp.setOpacity(0);
        ConfirmerMdp.setOpacity(0);
        validerNvMdp.setOpacity(0);
        QuestionSecrete.setOpacity(0);
        reponseQuestionSecrete.setOpacity(0);
        validerQuestion.setOpacity(0);
        //le texte d'erreur sera de couleur rouge
        Erreur.setTextFill(Color.RED);
    }


    /**
     * sert à récupérer la question secrète dans la base de données, et à assigner la réponse attendue
     */
    @FXML
    private void getQuestionSecrete()
    {
        try
        {
            String query = "call getQuestionSecrete(?);";//requête
            pstmt = cnx.prepareStatement(query);//statement
            {
                pstmt.setString(1, identifiant.getText());//assignation de l'identifiant à la requête
            }
            rs= pstmt.executeQuery();
            if(rs.next())//s'il y a un résultat
            {
                Erreur.setText("");//effacer le message d'erreur s'il y en a un
                QuestionSecrete.setText(rs.getString("question"));//affichage de la question
                //affichage de ce qui était invisible pour la question secrète
                QuestionSecrete.setOpacity(1);
                reponseQuestionSecrete.setOpacity(1);
                validerQuestion.setOpacity(1);
                //assignation de l'id pour la suite
                id=identifiant.getText();
                //requête
                query="call getReponseSecrete(?);";
                pstmt = cnx.prepareStatement(query);
                {
                    pstmt.setString(1, id);//assignation de l'id
                }
                rs=pstmt.executeQuery();
                if(rs.next())//s'il y a un résultat
                {
                    reponseQuestion=rs.getString("UTI_REPONSE_SECRETE");//assignation de la réponse attendue
                }
            }
            else
            {
                Erreur.setText("Cet identifiant n'existe pas");//si l'identifiant rentré n'existe pas
            }
        }
        catch(SQLException e)
        {
            System.out.println("erreur getQuestionSecrete : "+e);  //message d'erreur
        }
    }  
      
/**
 * sert à valider la réponse entrée par l'utilisateur pour la question secrète
 */
    @FXML
    private void validerReponse()
    {
        //si la réponse est correcte
        if(reponseQuestionSecrete.getText().equals(reponseQuestion))
        {
            //effacer le message d'erreur s'il y en a un
            Erreur.setText("");
            //affichage pour entrer le nouveau mot de passe
            entreeNouveauMdp.setOpacity(1);
            entreeConfirmerMdp.setOpacity(1);
            nouveauMdp.setOpacity(1);
            ConfirmerMdp.setOpacity(1);
            validerNvMdp.setOpacity(1);              
        }
        //si la réponse est incorrecte
        else
        {
            Erreur.setText("Réponse erronée");
        }
    }
    
    /**
     * sert à vérifier si le mot de passe est valide, puis à l'envoyer
     * @throws SQLException 
     */
    @FXML
    private void validerMotDePasse() throws SQLException
    {
        //si les deux mot de passe entrés sont égaux
        if(entreeConfirmerMdp.getText().equals(entreeNouveauMdp.getText()))
        {
            //si l'utilisateur a laissé les champs vides
            if(entreeNouveauMdp.getText().isBlank() || ConfirmerMdp.getText().isBlank())
            {
                Erreur.setText("champ vide");
            }
            //si tout est valide
            else
            {
                //effacer le message d'erreur s'il y en a un
                Erreur.setText("");
                //assignation du nouveau mot de passe
                nvMdp = entreeNouveauMdp.getText();
                //envoi vert la base de données
                envoiMotDePasse(nvMdp, id);
                try
                {
                    //fermeture du statement
                    pstmt.close();
                    //retour vers l'écran de connexion
                    App.setRoot("AccueilConnexion");
                }
                //en cas d'erreur
                catch(IOException e)
                {
                    System.out.println("Erreur validerMotDePasse : "+e);
                }                
            }

        }
        //si les mots de passe sont différents
        else
        {
            Erreur.setText("Mots de passe différents");
        }
    }
    
    /**
     * sert à envoyer le mot de passe vers la base de données
     * @param mdp
     * @param id 
     */
    private void envoiMotDePasse(String mdp, String id)
    {
        try
        {
            //s'il y a un mot de passe
            if(!mdp.isEmpty())
            {
                //cryptage du mot de passe
                String originalData = mdp;
                String encryptedMdp = Encryptor.encrypt(originalData);
                //requête sql
                String query = "call modifMdp(?,?);";
                //prepared statement
                pstmt = cnx.prepareStatement(query);
                {
                    pstmt.setString(1, encryptedMdp);
                    pstmt.setString(2, id);
                }
                pstmt.executeUpdate();//exécution
            }
        }
        //en cas d'erreur
        catch(Exception e)
        {
            System.out.println("Erreur envoiMotDePasse : "+e);
        }
    }
    
    /**
     * retour vers l'accueil
     */
    @FXML
    private void retour()
    {
        try {
            App.setRoot("AccueilConnexion");
        } catch (IOException ex) {
            Logger.getLogger(MdpOubliController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}