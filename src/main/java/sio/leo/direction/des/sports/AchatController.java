/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sio.leo.direction.des.sports;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author berthod
 */
public class AchatController implements Initializable {

    @FXML
    private Label TarifTotal;
    @FXML
    private Label TarifPiscine;
    @FXML
    private Label TarifPatinoire;
    @FXML
    private Label TarifFitness;
    @FXML
    private Label TarifTotal_Piscine;
    @FXML
    private Label TarifTotal_Patinoire;
    @FXML
    private Label TarifTotal_Fitness;
    @FXML
    private TextField QuantitePatinoire;
    @FXML
    private TextField QuantitePiscine;
    @FXML
    private TextField QuantiteFitness;    
    @FXML 
    private Button Retour;
    @FXML 
    private Button Payer;
    @FXML
    private Label AchatFait;
    
    private ResultSet rs;
    
    Connection cnx = DAO.getConnection();
    Statement smt = DAO.getStatement();
    public AchatController(){

    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    @FXML
    public void switchToConsommerTickets() throws IOException{
        App.setRoot("ConsommerTickets");
    }
    
    @FXML
    public void Achatfini(){

            if (QuantiteFitness.getText().isEmpty()&&QuantitePiscine.getText().isEmpty()&&QuantitePatinoire.getText().isEmpty()){
                AchatFait.setText("Veuillez saisir au moins une quantité.");
            }
                int valeurFit = Integer.parseInt(QuantiteFitness.getText());
                int valeurPisc = Integer.parseInt(QuantitePiscine.getText());
                int valeurPat = Integer.parseInt(QuantitePatinoire.getText());
              if (valeurFit==0&&valeurPisc==0&&valeurPat==0){
                  
                AchatFait.setText("Veuillez saisir au moins une quantité.");
            }
            else{

                AchatFait.setText("L'achat a été enregistré.");
                
            }
    }
    @FXML
    public void setTarifUnitaire(){
        String prixU= "CALL GetTarif_Piscine('"++"');";
        rs=smt.executeQuery(query);
        if rs.next()){
            return rs.getString("");
    }
        TarifPiscine.setText(prixU);
    }
    
//    public String NouveauxTickets(int quantite) throws SQLException{
//    String r="UPDATE" ... SET  ... = quantite +";";
//    smt.executeUpdate(r);
//    return r;
//}
}
