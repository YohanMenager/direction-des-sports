/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sio.leo.direction.des.sports;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sio.leo.direction.des.sports.modele.CrudInscription;

/**
 * FXML Controller class
 *
 * @author baras
 */
public class InscriptionController implements Initializable {

    CrudInscription crd = new CrudInscription();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    
    @FXML
    private TextField tfId;
    
    @FXML
    private TextField tfMdp;
    
    @FXML
    private TextField tfNom;
    
    @FXML
    private TextField tfPrenom;
    
    @FXML
    private TextField tfCP;
    
    @FXML
    private DatePicker dateN;
    
    @FXML
    private TextField tfTel;
    
    @FXML
    private TextField tfQuestion;
    
    @FXML
    private Label res;
    
    @FXML
    private void switchToAccueil() throws IOException {
        App.setRoot("AccueilConnexion");
    }
   
    @FXML
    private void switchToAccueilDAO() throws IOException, SQLException, SQLIntegrityConstraintViolationException, Exception {
        String resSet = crd.insertUser(tfId.getText(), tfMdp.getText(), tfNom.getText(), tfPrenom.getText(), tfCP.getText(), dateN.getValue(), tfTel.getText(), tfQuestion.getText());
        res.setText(resSet);
        System.out.println(crd.getMdp(tfId.getText(), tfMdp.getText()));
        
        //App.setRoot("AccueilConnexion");
    }
    
}
