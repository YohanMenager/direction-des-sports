/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sio.leo.direction.des.sports;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
 
/**
 * FXML Controller class
 *
 * @author khalfallah
 */
public class ConsommerTicketsController implements Initializable {
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
    
    }
    @FXML
    private Label label1;
    
    public void setLabel1(String msg){
        label1.setText(msg);
    }
    public void confirmationconsoPiscine(){

    // Show no alert at the startup of the program
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Consommer un ticket");
        alert.setHeaderText("Etes-vous sûr de consommer un ticket piscine ?");
        Optional<ButtonType> option = alert.showAndWait();
              if (option.get() == null) {
                setLabel1("No selection!");
              } else if (option.get() == ButtonType.OK) {
                setLabel1("Ticket consommé!");
              } else if (option.get() == ButtonType.CANCEL) {
                setLabel1("Comsommation annulée!");
              } else {
                setLabel1("-");
             }
          }  
    
    public void confirmationconsoPatinoire(){

    // Show no alert at the startup of the program
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Consommer un ticket");
        alert.setHeaderText("Etes-vous sûr de consommer un ticket patinoire ?");
        Optional<ButtonType> option = alert.showAndWait();
              if (option.get() == null) {
                setLabel1("No selection!");
              } else if (option.get() == ButtonType.OK) {
                setLabel1("Ticket consommé!");
              } else if (option.get() == ButtonType.CANCEL) {
                setLabel1("Comsommation annulée!");
              } else {
                setLabel1("-");
             }
          }  
    public void confirmationconsoFitness(){

    // Show no alert at the startup of the program
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Consommer un ticket");
        alert.setHeaderText("Etes-vous sûr de consommer un ticket fitness ?");
        Optional<ButtonType> option = alert.showAndWait();
              if (option.get() == null) {
                setLabel1("No selection!");
              } else if (option.get() == ButtonType.OK) {
                setLabel1("Ticket consommé!");
              } else if (option.get() == ButtonType.CANCEL) {
                setLabel1("Comsommation annulée!");
              } else {
                setLabel1("-");
             }
          }  


}
