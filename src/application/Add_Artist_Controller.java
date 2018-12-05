package application;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.sql.*;

public class Add_Artist_Controller implements Initializable {

    public Button add_artist_button;
    public TextField artist_name_input;
    public ChoiceBox<String> genreList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/andrew_yeh/Desktop/Code/NOT Spotify/src/application/playlist_organizer.db");
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys = ON");
            ResultSet rs = statement.executeQuery("select g_name from genres");
            genreList.setValue(rs.getString("g_name"));
            while (rs.next()) {
                genreList.getItems().add(rs.getString("g_name"));
            }
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void add_artist(ActionEvent event) throws IOException {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/andrew_yeh/Desktop/Code/NOT Spotify/src/application/playlist_organizer.db");
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys = ON");
            ResultSet rs = statement.executeQuery("select ar_artID, g_genID from artists, genres where ar_genID=g_genID and g_name like '" + genreList.getValue() + "' and ar_name like '" + artist_name_input.getText() + "'");
            if (rs.isBeforeFirst()) {
                popup_windows.already_exists_popup();
            } else if (!artist_name_input.getText().isEmpty()) {
                String ar_name = artist_name_input.getText().replace("'", "''");
                rs = statement.executeQuery("select g_genID from genres where g_name like '" + genreList.getValue() + "'");
                statement.executeUpdate("insert into artists(ar_name, ar_genID) values ('" + ar_name + "'," + rs.getInt("g_genID") + ")");
                if (popup_windows.another_alert_popup()) {
                    popup_windows.add_artist_popup();
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