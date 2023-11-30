/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sio.leo.direction.des.sports;

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
    private TextField reponseQuestionSecrete;
    
    @FXML
    private PasswordField entreeNouveauMdp;
    
    @FXML
    private PasswordField entreeConfirmerMdp;
    
    @FXML
    private Button validerQuestion;
    
    @FXML
    private Button validerNvMdp;
    
    @FXML
    private Label Erreur;
    
    
    private String questionSecrete = "Test Question Secrète";
    private String reponseQuestion = "réponse";
    private String nvMdp;
    private ResultSet rs;
    
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
        QuestionSecrete.setText(questionSecrete);
        Erreur.setTextFill(Color.RED);
    }

    /*private String Test() throws SQLException
    {
        try
        {
            String Query = "select * from UTILISATEUR where CAT_CODE=1;";
            ResultSet rs = smt.executeQuery(Query);
                
            if(rs.next()){
                
                return rs.getString(1);
            }
        }
        catch(SQLException e)
        {
            System.out.println("erreur : "+e);  
        }
        return null;
    }*/
    
    private String getQuestionSecrete()
    {
        try
        {
            String query = "call getQuestionSecrete()";
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
            
            envoiMotDePasee(nvMdp);
            try
            {
                App.setRoot("");
            }
            catch(Exception e)
            {
                System.out.println("Erreur : "+e);
            }
        }
        else
        {
            Erreur.setText("Mots de passe différents");
        }
    }
    
    private void envoiMotDePasee(String mdp)
    {
        try
        {
            String query="call updateMdp()";
            smt.executeUpdate(query);
        }
        catch(SQLException e)
        {
            System.out.println("erreur : "+e);
            Erreur.setText("Erreur dans le changement du mot de passe");
        }
    }
    
}
