package application;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Ask_Add_Song_Playlist_Controller {

    public static boolean yes;

    public void yes_pressed(ActionEvent event)
    {
        yes=true;
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.close();
    }

    public void no_pressed(ActionEvent event)
    {
        yes=false;
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.close();
    }

}
