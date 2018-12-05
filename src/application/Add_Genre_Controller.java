package application;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class Add_Genre_Controller {

    public Button add_genre_button;
    public TextField genre_name_input;

    public void add_genre(ActionEvent event) throws IOException {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/andrew_yeh/Desktop/Code/NOT Spotify/src/application/playlist_organizer.db");
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys = ON");
            ResultSet rs = statement.executeQuery("select g_genID from genres where g_name like '" + genre_name_input.getText() + "'");
            if (rs.isBeforeFirst()) {
                //genre already exists
                popup_windows.already_exists_popup();
            } else if (!genre_name_input.getText().isEmpty()) {
                String g_name = genre_name_input.getText().replace("'", "''");
                statement.executeUpdate("insert into genres(g_name) values ('" + g_name + "')");
                if (popup_windows.another_alert_popup()) {
                    popup_windows.add_genre_popup();
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