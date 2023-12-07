/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sio.leo.direction.des.sports;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sio.leo.direction.des.sports.modele.DAO;
import sio.leo.direction.des.sports.modele.CrudConnexion;


/**
 * FXML Controller class
 *
 * @author CAYUELA
 */
public class AccueilConnexionController implements Initializable {

    @FXML
    private TextField idField;
    @FXML
    private PasswordField mdpField;
    @FXML
    private Button btnMdpOublie;
    @FXML
    private Button btnConnexion;
    @FXML
    private Button btnInscription;
    @FXML
    private Label erreurLabel;
    
   Connection cnx = DAO.getConnection();
    Statement smt = DAO.getStatement();
    
    CrudConnexion crudCnx = new CrudConnexion();
//    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
  
    @FXML
    private void validerConnexion() throws IOException, Exception{
        if(crudCnx.getMdp(idField.getText(), mdpField.getText())){
            App.setUtilisateur(crudCnx.requeteUtilisateur(idField.getText()));
            System.out.println("nom = " + App.getUtilisateur().getNom() + ", prénom = " + App.getUtilisateur().getNom() + ", id = " + App.getUtilisateur().getId());
            App.setRoot("Achat");
        }
        else{
            erreurLabel.setText("Identifiant / Mot de passe incorrect");
        }
    }
    
    
    @FXML
    private void switchToInscription() throws IOException{
        App.setRoot("Inscription");
    }
     @FXML
    private void switchToConsommerTickets() throws IOException{
        App.setRoot("ConsommerTickets");
    }
    @FXML
    private void switchToMdpOublie() throws IOException{
        App.setRoot("MdpOubli");
    }
    
    
 
    /**
     * Initializes the controller class.
     */
   
}
