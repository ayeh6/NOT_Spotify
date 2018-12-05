package application;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class Create_Playlist_Controller {

    public TextField playlist_name;

    public void create_playlist(ActionEvent event) throws IOException {
        if (!playlist_name.getText().isEmpty()) {
            String p_name = playlist_name.getText().replace("'","''");
            try {
                Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/andrew_yeh/Desktop/Code/NOT Spotify/src/application/playlist_organizer.db");
                Statement statement = connection.createStatement();
                statement.execute("PRAGMA foreign_keys = ON");
                statement.setQueryTimeout(30);
                statement.executeUpdate("insert into playlists(p_name,p_userID) values ('" + p_name + "'," + Login_Screen_Controller.current_user_ID + ")");
                ResultSet rs = statement.executeQuery("select p_playlistID, p_name, u_username from playlists, users where p_userID=u_userID and p_name like '" + p_name + "' and p_userID=" + Login_Screen_Controller.current_user_ID);
                User_Menu_Controller.selected_playlist = new database_object(rs.getInt("p_playlistID"), null, null, null, null, rs.getString("p_name"), rs.getString("u_username"), "playlists", null);
                connection.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
            if (popup_windows.ask_add_song_playlist_popup()) {
                //change window to search for song
                popup_windows.add_song_playlist_popup();
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.close();
            } else {
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.close();
            }
        }
    }
}
