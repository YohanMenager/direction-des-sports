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
import sio.leo.direction.des.sports.modele.DAO;

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
    private Label AchatFait;

    public ResultSet rs;

    Connection cnx = DAO.getConnection();
    Statement smt = DAO.getStatement();
    Double TTF = 0.0;
    Double TTPI = 0.0;
    Double TTPA = 0.0;
    Double total = 0.0;

    public AchatController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        lance automatiquement les méthodes pour le tarif unitaire des sports
        try {
            setTarifUnitaire(App.getUtilisateur().getId(), "fit");
            setTarifUnitaire(App.getUtilisateur().getId(), "pat");
            setTarifUnitaire(App.getUtilisateur().getId(), "pis");

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
    public void switchToConsommerTickets() throws IOException {
        App.setRoot("ConsommerTickets");
    }
//    méthode pour finaliser l'achat

    @FXML
    public void Achatfini() {
        try {
            // alerte en cas de champs non rempli
            if (QuantiteFitness.getText().isEmpty() && QuantitePiscine.getText().isEmpty() && QuantitePatinoire.getText().isEmpty()) {
                AchatFait.setText("Veuillez saisir au moins une quantité.");
            }

            // converti les textes des TextFields de quantités en int
            int valeurFit = Integer.parseInt(QuantiteFitness.getText());
            int valeurPisc = Integer.parseInt(QuantitePiscine.getText());
            int valeurPat = Integer.parseInt(QuantitePatinoire.getText());

            // Check if all quantities are zero
            if (valeurFit == 0 && valeurPisc == 0 && valeurPat == 0) {
                AchatFait.setText("Veuillez saisir au moins une quantité.");
                return;
            }
//modifier solde dans ACHETERTICKET
            if (valeurPat > 0) {
                try ( PreparedStatement secondCallableStatement = cnx.prepareStatement("{CALL CreationTicket(?, ?, ?)}")) {
                    secondCallableStatement.setString(1, "pat");
                    secondCallableStatement.setString(2, App.getUtilisateur().getId());
                    secondCallableStatement.setInt(3, valeurPat);
                    secondCallableStatement.executeUpdate();
                }
                try ( PreparedStatement secondCallableStatement = cnx.prepareStatement("{CALL AjoutTicketSolde(?, ?, ?)}")) {
                    secondCallableStatement.setString(1, App.getUtilisateur().getId());
                    secondCallableStatement.setString(2, "pat");
                    secondCallableStatement.setInt(3, valeurPat);
                    secondCallableStatement.executeUpdate();
                }
            }
            if (valeurPisc > 0) {

                try ( PreparedStatement secondCallableStatement = cnx.prepareStatement("{CALL CreationTicket(?, ?, ?)}")) {
                    secondCallableStatement.setString(1, "pis");
                    secondCallableStatement.setString(2, App.getUtilisateur().getId());
                    secondCallableStatement.setInt(3, valeurPisc);
                    secondCallableStatement.executeUpdate();
                }

                try ( PreparedStatement secondCallableStatement = cnx.prepareStatement("{CALL AjoutTicketSolde(?, ?, ?)}")) {
                    secondCallableStatement.setString(1, App.getUtilisateur().getId());
                    secondCallableStatement.setString(2, "pis");
                    secondCallableStatement.setInt(3, valeurPat);
                    secondCallableStatement.executeUpdate();
                }
            }
            if (valeurFit > 0) {

                try ( PreparedStatement secondCallableStatement = cnx.prepareStatement("{CALL CreationTicket(?, ?, ?)}")) {
                    secondCallableStatement.setString(1, "fit");
                    secondCallableStatement.setString(2, App.getUtilisateur().getId());
                    secondCallableStatement.setInt(3, valeurFit);
                    secondCallableStatement.executeUpdate();
                }
                try ( PreparedStatement secondCallableStatement = cnx.prepareStatement("{CALL AjoutTicketSolde(?, ?, ?)}")) {
                    secondCallableStatement.setString(1, App.getUtilisateur().getId());
                    secondCallableStatement.setString(2, "fit");
                    secondCallableStatement.setInt(3, valeurPat);
                    secondCallableStatement.executeUpdate();
                }
            }
//modifier solde ticket

// ajoute un message pour confirmer la transaction
            AchatFait.setText("L'achat a été enregistré.");

        } catch (SQLException | NumberFormatException e) {
            // Handle exceptions
            e.printStackTrace();
            AchatFait.setText("Erreur lors de l'achat.");
        }
    }
// la méthode SetTarifUnitaire set le tarif d'un ticket par rapport à sa catégorie

    @FXML
    public void setTarifUnitaire(String user, String sport) throws SQLException {

        // Préparez l'appel à la procédure stockée
        String callStatement = "{CALL GetTarif(?,?)}";
        try ( CallableStatement callableStatement = cnx.prepareCall(callStatement)) {
            // Remplacez 'adm10' par la valeur réelle à utiliser comme argument
            callableStatement.setString(1, user);
            callableStatement.setString(2, sport);
            // Exécutez la procédure stockée
            callableStatement.execute();

            // Récupérez le résultat de la procédure
            ResultSet resultSet = callableStatement.getResultSet();
            if (resultSet.next()) {
                if ("pis".equals(sport)) {
                    // Assurez-vous que TarifPiscine est un élément graphique que vous pouvez modifier
                    TarifPiscine.setText(resultSet.getString(1));
                } else if ("fit".equals(sport)) {
                    // Assurez-vous que TarifPiscine est un élément graphique que vous pouvez modifier
                    TarifFitness.setText(resultSet.getString(1));
                } else if ("pat".equals(sport)) {
                    // Assurez-vous que TarifPiscine est un élément graphique que vous pouvez modifier
                    TarifPatinoire.setText(resultSet.getString(1));
                }
            }
        } catch (SQLException e) {
            // Gérez les exceptions liées à la base de données
            e.printStackTrace();
        }
    }

    //méthode pour afficher le total de la commande par rapport au sport
    @FXML
    public void setTarifTotal_Piscine() {
        int quantite = Integer.parseInt(QuantitePiscine.getText());
        // Préparez l'appel à la procédure stockée
        String callStatement = "{CALL GetTarifTotal_Piscine(?,?)}";
        try ( CallableStatement callableStatement = cnx.prepareCall(callStatement)) {
            // Remplacez 'adm10' par la valeur réelle à utiliser comme argument
            callableStatement.setString(1, App.getUtilisateur().getId());
            callableStatement.setInt(2, quantite);
            // Exécutez la procédure stockée
            callableStatement.execute();

            // Récupérez le résultat de la procédure
            ResultSet resultSet = callableStatement.getResultSet();
            if (resultSet.next()) {
                // Assurez-vous que TarifPiscine est un élément graphique que vous pouvez modifier
                TarifTotal_Piscine.setText(resultSet.getString(1));
                setTarifTotal();
            }
        } catch (SQLException e) {
            // Gérez les exceptions liées à la base de données
            e.printStackTrace();
        }
    }

    @FXML
    public void setTarifTotal_Patinoire() {
        int quantite = Integer.parseInt(QuantitePatinoire.getText());
        // Préparez l'appel à la procédure stockée
        String callStatement = "{CALL GetTarifTotal_Patinoire(?,?)}";
        try ( CallableStatement callableStatement = cnx.prepareCall(callStatement)) {
            // Remplacez 'adm10' par la valeur réelle à utiliser comme argument
            callableStatement.setString(1, App.getUtilisateur().getId());
            callableStatement.setInt(2, quantite);
            // Exécutez la procédure stockée
            callableStatement.execute();

            // Récupérez le résultat de la procédure
            ResultSet resultSet = callableStatement.getResultSet();
            if (resultSet.next()) {
                // Assurez-vous que TarifPatinoire est un élément graphique que vous pouvez modifier
                TarifTotal_Patinoire.setText(resultSet.getString(1));
                setTarifTotal();

            }
        } catch (SQLException e) {
            // Gérez les exceptions liées à la base de données
            e.printStackTrace();
        }
    }

    @FXML
    public void setTarifTotal_Fitness() {
        int quantite = Integer.parseInt(QuantiteFitness.getText());
        // Préparez l'appel à la procédure stockée
        String callStatement = "{CALL GetTarifTotal_Fitness(?,?)}";
        try ( CallableStatement callableStatement = cnx.prepareCall(callStatement)) {
            // Remplacez 'adm10' par la valeur réelle à utiliser comme argument
            callableStatement.setString(1, App.getUtilisateur().getId());
            callableStatement.setInt(2, quantite);
            // Exécutez la procédure stockée
            callableStatement.execute();

            // Récupérez le résultat de la procédure
            ResultSet resultSet = callableStatement.getResultSet();
            if (resultSet.next()) {
                TarifTotal_Fitness.setText(resultSet.getString(1));
                setTarifTotal();
            }
        } catch (SQLException e) {
            // Gérez les exceptions liées à la base de données
            e.printStackTrace();
        }
    }

    @FXML
    public void setTarifTotal() {
//        Double total = 0.0;
//        Double TTF = 0.0;
//        Double TTPI = 0.0;
//        Double TTPA = 0.0;
        try {

            if (!TarifTotal_Fitness.getText().isEmpty()) {
                TTF = Double.parseDouble(TarifTotal_Fitness.getText());

            }

            // Vérification et conversion du champ TarifTotal_Patinoire
            if (!TarifTotal_Patinoire.getText().isEmpty()) {
                TTPI = Double.parseDouble(TarifTotal_Patinoire.getText());

            }

            // Vérification et conversion du champ TarifTotal_Piscine
            if (!TarifTotal_Piscine.getText().isEmpty()) {
                TTPA = Double.parseDouble(TarifTotal_Piscine.getText());

            }

            total = TTPA + TTF + TTPI;
            TarifTotal.setText(String.valueOf(total));

        } catch (NumberFormatException e) {
            // Handle the exception when parsing integers fails
            e.printStackTrace();
        }

    }

}
