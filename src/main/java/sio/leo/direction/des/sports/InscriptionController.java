/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sio.leo.direction.des.sports;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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
        try {
            ArrayList<String> mesChoix = crd.getQuestionSecrete();
            for(int i=0;i<mesChoix.size();i++){
                choix.getItems().add(mesChoix.get(i));
            }
            
            
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(InscriptionController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    } 
    
    @FXML
    private TextField tfId;
    
    @FXML
    private PasswordField tfMdp;
    
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
    private ComboBox<String> choix;
    
    private int choixId = 0;
    
    private String resCombo = "";

    @FXML
    private void switchToAccueil() throws IOException {
        App.setRoot("AccueilConnexion");
    }
   
    @FXML
    private void switchToAccueilDAO() throws IOException, SQLException, SQLIntegrityConstraintViolationException, Exception {
        
        String resSet = crd.insertUser(tfId.getText(), tfMdp.getText(), tfNom.getText(), tfPrenom.getText(), tfCP.getText(), dateN.getValue(), tfTel.getText(), tfQuestion.getText(),choixId);
        res.setText(resSet);
        if(resSet.equals("Inscription réussie")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, resSet);
            alert.show();
            App.setRoot("AccueilConnexion");
        }
        else{
            Alert alertErreur = new Alert(Alert.AlertType.INFORMATION, resSet);
            alertErreur.show();
        }
        
        
        
    }
    
    @FXML
    private void selectCombo() throws SQLException {
        

        choixId = 1 + choix.getSelectionModel().getSelectedIndex();
        //resCombo = choix.getSelectionModel().getSelectedItem();
        System.out.println(choixId);
    }
    
    /*
    @FXML
    private void initComboBox() throws SQLException {
        
        choix = crd.getQuestionSecrete();
    }
    
    /*
    private void choixCombo(){
            String selectedName = choix.getSelectionModel().getSelectedItem();
            int selectedItemId = getIdFromDatabase(selectedName); // Remplacez cette méthode par une requête pour obtenir l'ID à partir du nom
            System.out.println("Selected ID: " + selectedItemId);

    }
*/
}