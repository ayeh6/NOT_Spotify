package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

import java.net.URL;
import java.util.ResourceBundle;

public class View_Playlist_Songs_Controller implements Initializable {

    public database_object playlist;
    public Label playlist_name_label;
    public TableView<database_object> table;
    public TableColumn<database_object, String> songcol;
    public TableColumn<database_object, String> albumcol;
    public TableColumn<database_object, String> artistcol;
    public TableColumn<database_object, String> genrecol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playlist=User_Menu_Controller.selected_playlist;
        playlist_name_label.setText(playlist.getPlaylistname());
        songcol.setCellValueFactory(new PropertyValueFactory<>("songname"));
        albumcol.setCellValueFactory(new PropertyValueFactory<>("albumname"));
        artistcol.setCellValueFactory(new PropertyValueFactory<>("artistname"));
        genrecol.setCellValueFactory(new PropertyValueFactory<>("genrename"));
        ObservableList<database_object> resultsList = FXCollections.observableArrayList();

        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/andrew_yeh/Desktop/Code/NOT Spotify/src/application/playlist_organizer.db");
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys = ON");
            statement.setQueryTimeout(30);
            ResultSet rs = statement.executeQuery("select s_songID, s_name, al_name, ar_name, g_name from songs,albums,artists,genres,adds,playlists where s_albID=al_albID and s_artID=ar_artID and s_songID=ad_songID and s_genID=g_genID and p_playlistID=ad_playlistID and p_playlistID="+playlist.getID());
            while(rs.next()) {
                resultsList.add(new database_object(rs.getInt("s_songID"), rs.getString("s_name"), rs.getString("al_name"), rs.getString("ar_name"), rs.getString("g_name"), null, null, "songs", null));
            }
            table.setItems(resultsList);
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

}
