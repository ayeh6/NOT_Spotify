package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Add_Song_Playlist_Controller implements Initializable {

    public TextField song_name_input;
    public Label playlistlabel;
    public ListView<String> playlist_songs;
    public TableView<database_object> songs_table;
    public TableColumn<database_object, String> songCol;
    public TableColumn<database_object, String> albumCol;
    public TableColumn<database_object, String> artistCol;
    public TableColumn<database_object, String> genreCol;
    public ChoiceBox<String> sort_by_list;
    public database_object playlist;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sort_by_list.getItems().addAll("Song", "Album", "Artist", "Genre");
        sort_by_list.setValue("Song");
        sort_by_list.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            searchSongs();
        });

        playlist=User_Menu_Controller.selected_playlist;
        playlistlabel.setText(playlist.getPlaylistname());

        songCol.setCellValueFactory(new PropertyValueFactory<>("songname"));
        albumCol.setCellValueFactory(new PropertyValueFactory<>("albumname"));
        artistCol.setCellValueFactory(new PropertyValueFactory<>("artistname"));
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genrename"));

        list_playlist_songs();
        searchSongs();
    }

    public void list_playlist_songs() {
        playlist_songs.getItems().clear();
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/andrew_yeh/Desktop/Code/NOT Spotify/src/application/playlist_organizer.db");
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys = ON");
            statement.setQueryTimeout(30);
            ResultSet rs = statement.executeQuery("select s_name, ar_name from playlists, adds, songs, artists where p_playlistID=ad_playlistID and ad_songID=s_songID and s_artID=ar_artID and p_playlistID=" + playlist.getID());
            while (rs.next()) {
                playlist_songs.getItems().add(rs.getString("s_name") + " by " + rs.getString("ar_name"));
            }
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void addSong(ActionEvent event) throws IOException {
        //add song to adds table
        database_object song = songs_table.getSelectionModel().getSelectedItem();
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/andrew_yeh/Desktop/Code/NOT Spotify/src/application/playlist_organizer.db");
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys = ON");
            statement.setQueryTimeout(30);
            statement.executeUpdate("insert into adds values ("+playlist.getID()+","+song.getID()+")");
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        list_playlist_songs();
    }

    public void searchSongs() {
        ObservableList<database_object> resultsList = FXCollections.observableArrayList();
        String s_name = song_name_input.getText();
        String sort_by = "s_name";
        if (sort_by_list.getValue().equals("Song")) {
            sort_by = "s_name";
        } else if (sort_by_list.getValue().equals("Album")) {
            sort_by = "al_name";
        } else if (sort_by_list.getValue().equals("Artist")) {
            sort_by = "ar_name";
        } else if (sort_by_list.getValue().equals("Genre")) {
            sort_by = "g_name";
        }
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/andrew_yeh/Desktop/Code/NOT Spotify/src/application/playlist_organizer.db");
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys = ON");
            statement.setQueryTimeout(30);
            ResultSet rs = statement.executeQuery("select s_songID, s_name, al_name, ar_name, g_name from songs, albums, artists, genres where s_artID=ar_artID and s_albID=al_albID and s_genID=g_genID and s_name like '" + s_name + "%' order by " + sort_by + " asc");
            while (rs.next()) {
                resultsList.add(new database_object(rs.getInt("s_songID"), rs.getString("s_name"), rs.getString("al_name"), rs.getString("ar_name"), rs.getString("g_name"), null, null, "songs", null));
            }
            songs_table.setItems(resultsList);
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
