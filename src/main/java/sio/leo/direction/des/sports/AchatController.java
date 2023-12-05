/*
* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
* Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
*/
package sio.leo.direction.des.sports;

import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

    private int totalT;
    public ResultSet rs;

    Connection cnx = DAO.getConnection();
    Statement smt = DAO.getStatement();

    public AchatController(){
        this.totalT=0;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        lance automatiquement les méthodes pour le tarif unitaire des sports
        try {
        setTarifUnitaire("fit");
        setTarifUnitaire("pat");
        setTarifUnitaire("pis");

        setTarifTotal();

        // Add listeners to the labels to recalculate the total whenever they change
        TarifTotal_Fitness.textProperty().addListener((observable, oldValue, newValue) -> setTarifTotal());
        TarifTotal_Patinoire.textProperty().addListener((observable, oldValue, newValue) -> setTarifTotal());
        TarifTotal_Piscine.textProperty().addListener((observable, oldValue, newValue) -> setTarifTotal());
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
public void Achatfini() {
    try {
        // Check for empty fields
        if (QuantiteFitness.getText().isEmpty() && QuantitePiscine.getText().isEmpty() && QuantitePatinoire.getText().isEmpty()) {
            AchatFait.setText("Veuillez saisir au moins une quantité.");
            return;
        }

        // Parse quantities
        int valeurFit = Integer.parseInt(QuantiteFitness.getText());
        int valeurPisc = Integer.parseInt(QuantitePiscine.getText());
        int valeurPat = Integer.parseInt(QuantitePatinoire.getText());

        // Check if all quantities are zero
        if (valeurFit == 0 && valeurPisc == 0 && valeurPat == 0) {
            AchatFait.setText("Veuillez saisir au moins une quantité.");
            return;
        }

        // Your connection initialization here

        // Update fitness quantity
        try (PreparedStatement callableStatement = cnx.prepareStatement("{CALL AjoutTicket(?, ?, ?)}")) {
            callableStatement.setString(1, "ADM10");
            callableStatement.setString(2, "fit");
            callableStatement.setInt(3, valeurFit);
            callableStatement.executeUpdate();
        }

        // Update patinoire quantity
        try (PreparedStatement callableStatement = cnx.prepareStatement("{CALL AjoutTicket(?, ?, ?)}")) {
            callableStatement.setString(1, "ADM10");
            callableStatement.setString(2, "pat");
            callableStatement.setInt(3, valeurPat);
            callableStatement.executeUpdate();
        }

        // Update piscine quantity
        try (PreparedStatement callableStatement = cnx.prepareStatement("{CALL AjoutTicket(?, ?, ?)}")) {
            callableStatement.setString(1, "ADM10");
            callableStatement.setString(2, "pis");
            callableStatement.setInt(3, valeurPisc);
            callableStatement.executeUpdate();
        }

        AchatFait.setText("L'achat a été enregistré.");

    } catch (SQLException | NumberFormatException e) {
        // Handle exceptions
        e.printStackTrace();
        AchatFait.setText("Erreur lors de l'achat.");
    }
}


        @FXML
    public void setTarifUnitaire(String sport) throws SQLException
    {

        // Préparez l'appel à la procédure stockée
        String callStatement = "{CALL GetTarif(?,?)}";
        try (CallableStatement callableStatement = cnx.prepareCall(callStatement)) {
            // Remplacez 'adm10' par la valeur réelle à utiliser comme argument
            callableStatement.setString(1, "adm10");
            callableStatement.setString(2, sport);
            // Exécutez la procédure stockée
            callableStatement.execute();

            // Récupérez le résultat de la procédure
            ResultSet resultSet = callableStatement.getResultSet();
            if(resultSet.next()) {
                if ("pis".equals(sport)) {
                // Assurez-vous que TarifPiscine est un élément graphique que vous pouvez modifier
                    TarifPiscine.setText(resultSet.getString(1));
                }
                else if ("fit".equals(sport)) {
                // Assurez-vous que TarifPiscine est un élément graphique que vous pouvez modifier
                    TarifFitness.setText(resultSet.getString(1));
                }
                else if ("pat".equals(sport)) {
                // Assurez-vous que TarifPiscine est un élément graphique que vous pouvez modifier
                    TarifPatinoire.setText(resultSet.getString(1));
                }    }
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
                TarifTotal_Fitness.setText(resultSet.getString(1));
            }
        } 
        catch (SQLException e) 
        {
        // Gérez les exceptions liées à la base de données
        e.printStackTrace();
        }
    }

    @FXML
public void setTarifTotal() {
Double total=0.0;
    Double TTF=0.0;
        Double TTPI=0.0;
            Double TTPA=0.0;
    try {

         TTF = Double.parseDouble(TarifTotal_Fitness.getText());
         TTPI = Double.parseDouble(TarifTotal_Patinoire.getText());
         TTPA = Double.parseDouble(TarifTotal_Piscine.getText());
         total = TTPA + TTF + TTPI  ;
        TarifTotal.setText(String.valueOf(total));

    } catch (NumberFormatException e) {
        // Handle the exception when parsing integers fails
        e.printStackTrace();
    }

}

}
