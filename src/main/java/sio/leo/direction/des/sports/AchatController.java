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
    
    public ResultSet rs;
    
    Connection cnx = DAO.getConnection();
    Statement smt = DAO.getStatement();
    
    public AchatController(){

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        lance automatiquement les méthodes pour le tarif unitaire des sports
        try {
        setTarifUnitaire_Piscine();
        setTarifUnitaire_Patinoire();
        setTarifUnitaire_Fitness();
//        setTarifTotal();
    } catch (SQLException e) {
        e.printStackTrace();
        // Gérer l'erreur lors de l'appel de la méthode
    }
        
        
        }
    
//    méthode pour retourner à la page précédente
    @FXML
    public void switchToConsommerTickets() throws IOException{
        App.setRoot("ConsommerTickets");
    }
//    méthode pour finaliser l'achat
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
//    méthodes pour afficher le tarif des sports
    @FXML
    public void setTarifUnitaire_Piscine() throws SQLException
    {

        // Préparez l'appel à la procédure stockée
        String callStatement = "{CALL GetTarif_Piscine(?)}";
        try (CallableStatement callableStatement = cnx.prepareCall(callStatement)) {
            // Remplacez 'adm10' par la valeur réelle à utiliser comme argument
            callableStatement.setString(1, "adm10");

            // Exécutez la procédure stockée
            callableStatement.execute();

            // Récupérez le résultat de la procédure
            ResultSet resultSet = callableStatement.getResultSet();
            if(resultSet.next()) {
                // Assurez-vous que TarifPiscine est un élément graphique que vous pouvez modifier
                TarifPiscine.setText(resultSet.getString(1));
    }
                } catch (SQLException e) {
        // Gérez les exceptions liées à la base de données
        e.printStackTrace();
    }
}
    
        @FXML
    public void setTarifUnitaire_Patinoire() throws SQLException
    {

        // Préparez l'appel à la procédure stockée
        String callStatement = "{CALL GetTarif_Patinoire(?)}";
        try (CallableStatement callableStatement = cnx.prepareCall(callStatement)) {
            // Remplacez 'adm10' par la valeur réelle à utiliser comme argument
            callableStatement.setString(1, "adm10");

            // Exécutez la procédure stockée
            callableStatement.execute();

            // Récupérez le résultat de la procédure
            ResultSet resultSet = callableStatement.getResultSet();
            if(resultSet.next()) {
                // Assurez-vous que TarifPiscine est un élément graphique que vous pouvez modifier
                TarifPatinoire.setText(resultSet.getString(1));
    }
                } catch (SQLException e) {
        // Gérez les exceptions liées à la base de données
        e.printStackTrace();
    }
}
    
@FXML
    public void setTarifUnitaire_Fitness() throws SQLException
    {

        // Préparez l'appel à la procédure stockée
        String callStatement = "{CALL GetTarif_Fitness(?)}";
        try (CallableStatement callableStatement = cnx.prepareCall(callStatement))
        {
            // Remplacez 'adm10' par la valeur réelle à utiliser comme argument
            callableStatement.setString(1, "adm10");

            // Exécutez la procédure stockée
            callableStatement.execute();

            // Récupérez le résultat de la procédure
            ResultSet resultSet = callableStatement.getResultSet();
            if(resultSet.next()) {
                // Assurez-vous que TarifPiscine est un élément graphique que vous pouvez modifier
                TarifFitness.setText(resultSet.getString(1));
    }
                } catch (SQLException e) {
        // Gérez les exceptions liées à la base de données
        e.printStackTrace();
    }
}

    //méthode pour afficher le total de la commande par rapport au sport


@FXML
    public void setTarifTotal_Piscine()
    {
        int quantite = Integer.parseInt(QuantitePiscine.getText());
        // Préparez l'appel à la procédure stockée
        String callStatement = "{CALL GetTarifTotal_Piscine(?,?)}";
        try (CallableStatement callableStatement = cnx.prepareCall(callStatement))
        {
            // Remplacez 'adm10' par la valeur réelle à utiliser comme argument
            callableStatement.setString(1, "adm10");
            callableStatement.setInt(2, quantite);
            // Exécutez la procédure stockée
            callableStatement.execute();

            // Récupérez le résultat de la procédure
            ResultSet resultSet = callableStatement.getResultSet();
            if(resultSet.next())
            {
                // Assurez-vous que TarifPiscine est un élément graphique que vous pouvez modifier
                TarifTotal_Piscine.setText(resultSet.getString(1));
                //TarifTotal.setText(TarifTotal+resultSet.getString(1));
            }
        } 
        catch (SQLException e) 
        {
        // Gérez les exceptions liées à la base de données
        e.printStackTrace();
        }
    }
    
@FXML
    public void setTarifTotal_Patinoire()
    {
        int quantite = Integer.parseInt(QuantitePatinoire.getText());
        // Préparez l'appel à la procédure stockée
        String callStatement = "{CALL GetTarifTotal_Patinoire(?,?)}";
        try (CallableStatement callableStatement = cnx.prepareCall(callStatement))
        {
            // Remplacez 'adm10' par la valeur réelle à utiliser comme argument
            callableStatement.setString(1, "adm10");
            callableStatement.setInt(2, quantite);
            // Exécutez la procédure stockée
            callableStatement.execute();

            // Récupérez le résultat de la procédure
            ResultSet resultSet = callableStatement.getResultSet();
            if(resultSet.next())
            {
                // Assurez-vous que TarifPatinoire est un élément graphique que vous pouvez modifier
                TarifTotal_Patinoire.setText(resultSet.getString(1));
                //TarifTotal.setText(TarifTotal+resultSet.getString(1));

            }
        } 
        catch (SQLException e) 
        {
        // Gérez les exceptions liées à la base de données
        e.printStackTrace();
        }
    }
    
@FXML
    public void setTarifTotal_Fitness()
    {
        int quantite = Integer.parseInt(QuantiteFitness.getText());
        // Préparez l'appel à la procédure stockée
        String callStatement = "{CALL GetTarifTotal_Fitness(?,?)}";
        try (CallableStatement callableStatement = cnx.prepareCall(callStatement))
        {
            // Remplacez 'adm10' par la valeur réelle à utiliser comme argument
            callableStatement.setString(1, "adm10");
            callableStatement.setInt(2, quantite);
            // Exécutez la procédure stockée
            callableStatement.execute();

            // Récupérez le résultat de la procédure
            ResultSet resultSet = callableStatement.getResultSet();
            if(resultSet.next())
            {
                // Assurez-vous que TarifPatinoire est un élément graphique que vous pouvez modifier
                TarifTotal_Fitness.setText(resultSet.getString(1));
               // TarifTotal.setText(TarifTotal+resultSet.getString(1));

            }
        } 
        catch (SQLException e) 
        {
        // Gérez les exceptions liées à la base de données
        e.printStackTrace();
        }
    }
    
   @FXML
   public void setTarifTotal(){
        int TTF = Integer.parseInt(QuantiteFitness.getText());
        int TTPI = Integer.parseInt(QuantitePiscine.getText());
        int TTPA = Integer.parseInt(QuantitePatinoire.getText());
        int total = TTF+TTPI+TTPA;
        TarifTotal.setText(Integer.toString(total));
   }

    
}
