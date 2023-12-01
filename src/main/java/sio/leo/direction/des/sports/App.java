package sio.leo.direction.des.sports;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
//Pour connecter mariadb et la base lardon
    private static Connection cnx = null;
    //pour exécuter des requêtes
    private static Statement smt=null;
    
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("Achat"), 640, 480);
        stage.setScene(scene);
        stage.show();
        DAO.SeConnecter();
        cnx=DAO.getConnection();
        smt=DAO.getStatement();
    }

    
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
    
}