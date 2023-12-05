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
    private PreparedStatement pstmt;
    
    Connection cnx = DAO.getConnection();
    
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
        int a =0;
        try
        {
            //String query = "call getQuestionSecrete('"+identifiant.getText()+"')";
            String query = "call getQuestionSecrete(?);";
            pstmt = cnx.prepareStatement(query);
            {
                pstmt.setString(1, identifiant.getText());
            }
            a++;
            System.out.println(pstmt.toString());
            rs= pstmt.executeQuery();
            a++;
            System.out.println(a);
            if(rs.next())
            {
            a++;
            System.out.println(a);
                QuestionSecrete.setText(rs.getString("question"));
                QuestionSecrete.setOpacity(1);
                reponseQuestionSecrete.setOpacity(1);
                validerQuestion.setOpacity(1);
                id=identifiant.getText();
                query="call getReponseSecrete(?);";
                pstmt = cnx.prepareStatement(query);
                {
                    pstmt.setString(1, id);
                }
                rs=pstmt.executeQuery();
                if(rs.next())
                {
                    reponseQuestion=rs.getString("UTI_REPONSE_SECRETE");
                }
            }
        }
        catch(SQLException e)
        {
            System.out.println("erreur getQuestionSecrete : "+a+" "+e);  
        }
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
    private void validerMotDePasse() throws SQLException
    {
        if(entreeConfirmerMdp.getText().equals(entreeNouveauMdp.getText()))
        {
            Erreur.setText("");
            nvMdp = entreeNouveauMdp.getText();
            
            envoiMotDePasse(nvMdp, id);
            try
            {
                pstmt.close();
                App.setRoot("AccueilConnexion");
            }
            catch(IOException e)
            {
                System.out.println("Erreur validerMotDePasse : "+e);
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
                //String query = "call modifMdp('"+encryptedMdp+"', '"+id+"');";
                String query = "call modifMdp(?,?);";
                pstmt = cnx.prepareStatement(query);
                {
                    pstmt.setString(1, encryptedMdp);
                    pstmt.setString(2, id);
                }
                pstmt.executeUpdate();
            }
        }
        catch(Exception e)
        {
            System.out.println("Erreur envoiMotDePasse : "+e);
        }
    }
    
}