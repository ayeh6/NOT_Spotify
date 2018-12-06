package application;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Add_Album_Controller implements Initializable {

    public ChoiceBox<String> artistList;
    public TextField album_name_input;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/andrew_yeh/Desktop/Code/NOT Spotify/src/application/playlist_organizer.db");
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys = ON");
            ResultSet rs = statement.executeQuery("select ar_name from artists");
            artistList.setValue(rs.getString("ar_name"));
            while (rs.next()) {
                artistList.getItems().add(rs.getString("ar_name"));
            }
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void add_album(ActionEvent event) throws IOException {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/andrew_yeh/Desktop/Code/NOT Spotify/src/application/playlist_organizer.db");
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys = ON");
            ResultSet rs = statement.executeQuery("select al_albID, ar_artID,g_genID from albums,artists,genres where al_artID=ar_artID and ar_genID=g_genID and al_name like '"+album_name_input.getText()+"' and ar_name like '" + artistList.getValue() + "'");
            if (rs.isBeforeFirst()) {
                popup_windows.already_exists_popup();
            } else if (!album_name_input.getText().isEmpty()) {
                String al_name = album_name_input.getText().replace("'", "''");
                rs = statement.executeQuery("select ar_artID,g_genID from artists,genres where ar_genID=g_genID and ar_name like '" + artistList.getValue() + "'");
                statement.executeUpdate("insert into albums(al_name,al_artID,al_genID) values ('" + al_name + "'," + rs.getInt("ar_artID") + "," + rs.getInt("g_genID") + ")");
                if (popup_windows.another_alert_popup()) {
                    popup_windows.add_album_popup();
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.close();
                } else {
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.close();
                }
            }
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
