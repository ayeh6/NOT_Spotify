package application;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Admin_Alert_Controller {

    public static boolean yes = false;

    public void yes_pressed(ActionEvent event) {
        yes = true;
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.close();
    }

    public void no_pressed(ActionEvent event) {
        yes = false;
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.close();
    }
}