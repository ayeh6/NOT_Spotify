package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage login_stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("login_screen.fxml"));
        login_stage.setTitle("NOT Spotify");
        login_stage.setScene(new Scene(root));
        login_stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
