/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sio.leo.direction.des.sports;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author MENAGER
 */
public class MdpOubliController implements Initializable {

    @FXML
    private Label QuestionSecrete;
    
    @FXML
    private Label nouveauMdp;
    
    @FXML 
    private Label ConfirmerMdp;
    
    @FXML
    private Label Erreur;
        
    
    @FXML
    private TextField reponseQuestionSecrete;
    
    @FXML
    private TextField identifiant;
    
    @FXML
    private PasswordField entreeNouveauMdp;
    
    @FXML
    private PasswordField entreeConfirmerMdp;
    
    @FXML
    private Button validerQuestion;
    
    @FXML
    private Button validerNvMdp;
    
    @FXML
    private Button validerId;
    
    
    
    private String reponseQuestion;
    private String nvMdp;
    private ResultSet rs;
    private String id;
    
    Connection cnx = DAO.getConnection();
    Statement smt = DAO.getStatement();
    
    //private App app = new App();
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        entreeNouveauMdp.setOpacity(0);
        entreeConfirmerMdp.setOpacity(0);
        nouveauMdp.setOpacity(0);
        ConfirmerMdp.setOpacity(0);
        validerNvMdp.setOpacity(0);
        QuestionSecrete.setOpacity(0);
        reponseQuestionSecrete.setOpacity(0);
        validerQuestion.setOpacity(0);
        Erreur.setTextFill(Color.RED);
    }


    
    @FXML
    private void getQuestionSecrete()
    {
        try
        {
            String query = "select question from QUESTION_SECRETE join UTILISATEUR on UTILISATEUR.UTI_ID_QUESTION_SECRETE = QUESTION_SECRETE.id where UTI_ID ='"+ identifiant.getText() +"';";
            rs= smt.executeQuery(query);
            if(rs.next())
            {
                QuestionSecrete.setText(rs.getString("question"));
                QuestionSecrete.setOpacity(1);
                reponseQuestionSecrete.setOpacity(1);
                validerQuestion.setOpacity(1);
                id=identifiant.getText();
                query="Select UTI_REPONSE_SECRETE from UTILISATEUR where UTI_ID ='"+ id +"';";
                rs=smt.executeQuery(query);
                if(rs.next())
                {
                    reponseQuestion=rs.getString("UTI_REPONSE_SECRETE");
                }
            }
        }
        catch(SQLException e)
        {
            System.out.println("erreur : "+e);  
        }
    }
    
    private String getReponseSecrete()
    {
        try
        {
            String query = "call getReponseSecrete()";
            rs= smt.executeQuery(query);
            if(rs.next())
            {
                return rs.getString("");
            }
        }
        catch(SQLException e)
        {
            System.out.println("erreur : "+e);  
        }
        return null;
    }    
      

    @FXML
    private void validerReponse()
    {
        if(reponseQuestionSecrete.getText().equals(reponseQuestion))
        {
            Erreur.setText("");
            entreeNouveauMdp.setOpacity(1);
            entreeConfirmerMdp.setOpacity(1);
            nouveauMdp.setOpacity(1);
            ConfirmerMdp.setOpacity(1);
            validerNvMdp.setOpacity(1);              
        }
      else
        {
            Erreur.setText("Réponse erronée");
        }
    }
    
    @FXML
    private void validerMotDePasse()
    {
        if(entreeConfirmerMdp.getText().equals(entreeNouveauMdp.getText()))
        {
            Erreur.setText("");
            nvMdp = entreeNouveauMdp.getText();
            
            envoiMotDePasse(nvMdp, id);
            try
            {
                App.setRoot("AccueilConnexion");
            }
            catch(IOException e)
            {
                System.out.println("Erreur : "+e);
            }
        }
        else
        {
            Erreur.setText("Mots de passe différents");
        }
    }
    
    private void envoiMotDePasse(String mdp, String id)
    {
        try
        {
            if(!mdp.isEmpty())
            {
                String originalData = mdp;
                String encryptedMdp = Encryptor.encrypt(originalData);
                String requete = "call modifMdp('"+encryptedMdp+"', '"+id+"');";
                smt.executeUpdate(requete);
            }
        }
        catch(Exception e)
        {
            System.out.println("Erreur : "+e);
        }
    }
    
}