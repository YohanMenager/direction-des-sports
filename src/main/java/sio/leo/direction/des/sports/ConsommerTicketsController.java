/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sio.leo.direction.des.sports;

import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import sio.leo.direction.des.sports.modele.DAO;
 
/**
 * FXML Controller class
 *
 * @author khalfallah
 */
public class ConsommerTicketsController implements Initializable {
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        connect();
        try {
            GetSolde("pis", App.getUtilisateur().getId());
            GetSolde("fit", App.getUtilisateur().getId());         
            GetSolde("pat", App.getUtilisateur().getId());
        } catch (SQLException ex) {
            Logger.getLogger(ConsommerTicketsController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    @FXML
    private Label label1;
    @FXML
    private Label SoldePiscine;
    @FXML
    private Label SoldePatinoire;
    @FXML
    private Label SoldeFitness;
    Connection connection;
    private int achQuantite;
    private int achQuantitePis;
    private int achQuantitePat;
    private int achQuantiteFit;
    @FXML
    private Button goToAcheter;
    @FXML
    private Button goToConnexion;
    

    public void setLabel1(String msg){
        label1.setText(msg);
    }

    public void connect(){
       connection = DAO.getConnection();
    }
    public void GetSolde(String activite, String utilisateur) throws SQLException {
    try (
         CallableStatement callableStatement = connection.prepareCall("{CALL GetSoldeTicket(?, ?)}")) {

        // Définir les paramètres de la procédure stockée
        callableStatement.setString(1, activite); // Activité (pis ou pat ou fit)
        callableStatement.setString(2, utilisateur); // Nom d'utilisateur (ADM10 ou autre)

        // Exécuter la procédure stockée
        callableStatement.execute();

        try (ResultSet resultSet = callableStatement.getResultSet()) {
            if (resultSet.next()) {
                achQuantite = resultSet.getInt("SoldeQuantite");
                // Faites quelque chose avec la valeur obtenue, par exemple, mettez à jour vos labels
                if ("fit".equals(activite)) {
                    SoldeFitness.setText("Solde de tickets : " + achQuantite);
                    achQuantiteFit = achQuantite;
                } else if ("pat".equals(activite)) {
                    SoldePatinoire.setText("Solde de tickets : " + achQuantite);
                    achQuantitePat = achQuantite;
                }
                else if ("pis".equals(activite)) {
                    SoldePiscine.setText("Solde de tickets : " + achQuantite);
                    achQuantitePis = achQuantite;
                }
            } else {
                // Si aucun résultat retourné par la procédure stockée sa retoune ça
                System.out.println("Aucun résultat retourné par la procédure stockée.");
            }
        }
    }
}
    public void ConsommerTicket(String activite, String utilisateur) throws SQLException {
    try (
            //Connection connection = DAO.getConnection();
         CallableStatement callableStatement = connection.prepareCall("{CALL ConsommerTicket(?, ?)}")) {

        // Définir les paramètres de la procédure stockée
        callableStatement.setString(1, activite); // Activité (pis ou pat)
        callableStatement.setString(2, utilisateur); // Nom d'utilisateur (ADM10 ou autre)

        // Exécuter la procédure stockée
        callableStatement.execute();
        }
    }
    
    public void InsertConsommerTicket(String activite, String utilisateur) throws SQLException {
    try (
            //Connection connection = DAO.getConnection();
         CallableStatement callableStatement = connection.prepareCall("{CALL InsertConsommerticket(?, ?)}")) {

        // Définir les paramètres de la procédure stockée
        callableStatement.setString(1, activite); // Activité (pis ou pat)
        callableStatement.setString(2, utilisateur); // Nom d'utilisateur (ADM10 ou autre)

        // Exécuter la procédure stockée
        callableStatement.execute();
        }
    }



    public void SoldePiscine1() throws SQLException {
        // vérifie si l'utilisateur possède des tickets
        if(achQuantitePis > 0){
            //Consomme le ticket
        ConsommerTicket("pis", App.getUtilisateur().getId());
        // ajout de la consommation dans la bdd
        InsertConsommerTicket(App.getUtilisateur().getId(), "pis");
        //mettre à jour le solde dans l'app
        GetSolde("pis", App.getUtilisateur().getId());
        // ajout du texte d'en bas
        setLabel1("Ticket consommé!");}
        else{setLabel1("Votre solde est insuffisant!");}


    }
    public void SoldePatinoire1() throws SQLException{
        if(achQuantitePat > 0){
        ConsommerTicket("pat", App.getUtilisateur().getId());
        InsertConsommerTicket(App.getUtilisateur().getId(), "pat");        
        GetSolde("pat", App.getUtilisateur().getId());
        setLabel1("Ticket consommé!");}
        else{setLabel1("Votre solde est insuffisant!");}
    }
    public void SoldeFitness1() throws SQLException{
        if(achQuantiteFit != 0){
        ConsommerTicket("fit", App.getUtilisateur().getId());
        InsertConsommerTicket(App.getUtilisateur().getId(), "fit");
        GetSolde("fit", App.getUtilisateur().getId());
        setLabel1("Ticket consommé!");}
        else{setLabel1("Votre solde est insuffisant!");}

    }
    public void confirmationconsoPiscine() throws SQLException{

    // Créer l'alerte de type confirmation
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Consommer un ticket");
        alert.setHeaderText("Etes-vous sûr de consommer un ticket piscine ?");
        Optional<ButtonType> option = alert.showAndWait();
              if (option.get() == null) {
                setLabel1("No selection!");
              } else if (option.get() == ButtonType.OK) {
                SoldePiscine1();
              } else if (option.get() == ButtonType.CANCEL) {
                setLabel1("Consommation annulée!");
              } else {
                setLabel1("-");
             }
          }  
    
    public void confirmationconsoPatinoire() throws SQLException{

    // Créer l'alerte de type confirmation
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Consommer un ticket");
        alert.setHeaderText("Etes-vous sûr de consommer un ticket patinoire ?");
        Optional<ButtonType> option = alert.showAndWait();
              if (option.get() == null) {
                setLabel1("No selection!");
              } else if (option.get() == ButtonType.OK) {
                SoldePatinoire1();
              } else if (option.get() == ButtonType.CANCEL) {
                setLabel1("Consommation annulée!");
              } else {
                setLabel1("-");
            }
    }  
    public void confirmationconsoFitness() throws SQLException{

    // Créer l'alerte de type confirmation
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Consommer un ticket");
        alert.setHeaderText("Etes-vous sûr de consommer un ticket fitness ?");
        Optional<ButtonType> option = alert.showAndWait();
              if (option.get() == null) {
                setLabel1("No selection!");
              } else if (option.get() == ButtonType.OK) {
                SoldeFitness1();
              } else if (option.get() == ButtonType.CANCEL) {
                setLabel1("Consommation annulée!");
              } else {
                setLabel1("-");
             }
        }  

        
    public void goToAcheterTicket() throws SQLException, IOException{
        // aller sur la vue achat
        App.setRoot("Achat");
    }
    public void goToConnexion() throws SQLException, IOException{
        // aller sur la vue AccueilConnexion
        App.setRoot("AccueilConnexion");
    }

}
