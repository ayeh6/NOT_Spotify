package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class Admin_Menu_Controller implements Initializable {

    public String search_from, search_select, search_term;
    public TextField search_input;
    public TableView<database_object> resultsTable;
    public ChoiceBox<String> search_dropdown;
    public MenuBar menu_bar;
    public ContextMenu table_context;
    public MenuItem delete_item;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        search_dropdown.getItems().addAll("Songs", "Albums", "Artists", "Genres", "Playlists", "Users");
        search_dropdown.setValue("Songs");
        search_dropdown.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            searchQuery();
        });
        searchQuery();
    }

    public void delete() {
        database_object delete_item = resultsTable.getSelectionModel().getSelectedItem();
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/andrew_yeh/Desktop/Code/NOT Spotify/src/application/playlist_organizer.db");
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys = ON");
            statement.setQueryTimeout(30);
            statement.executeUpdate("delete from " + delete_item.getTable() + " where " + delete_item.getId_param() + "=" + delete_item.getID());
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        searchQuery();
    }

    public void view_playlist_table() throws IOException {
        Login_Screen_Controller.selected_playlist = resultsTable.getSelectionModel().getSelectedItem();
        popup_windows.view_playlist_songs_popup();
    }

    public void change_password_menu() throws IOException {
        popup_windows.change_password_popup();
    }

    public void add_song_menu() throws IOException {
        popup_windows.add_song_popup();
    }

    public void add_album_menu() throws IOException {
        popup_windows.add_album_popup();
    }

    public void add_artist_menu() throws IOException {
        popup_windows.add_artist_popup();
    }

    public void add_genre_menu() throws IOException {
        popup_windows.add_genre_popup();
    }

    public void log_out() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("login_screen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) menu_bar.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void searchQuery() {
        search_term = search_input.getText().replace("'", "''");
        search_from = search_dropdown.getValue();
        ObservableList<database_object> resultsList = FXCollections.observableArrayList();
        table_context.getItems().clear();
        table_context.getItems().add(delete_item);
        if (search_from.equals("Songs") || search_from.equals("Albums") || search_from.equals("Artists") || search_from.equals("Genres")) {
            TableColumn<database_object, String> songcol = new TableColumn<>("Song");
            songcol.setMinWidth(200);
            songcol.setCellValueFactory(new PropertyValueFactory<>("songname"));

            TableColumn<database_object, String> albumcol = new TableColumn<>("Album");
            albumcol.setMinWidth(200);
            albumcol.setCellValueFactory(new PropertyValueFactory<>("albumname"));

            TableColumn<database_object, String> artistcol = new TableColumn<>("Artist");
            artistcol.setMinWidth(200);
            artistcol.setCellValueFactory(new PropertyValueFactory<>("artistname"));

            TableColumn<database_object, String> genrecol = new TableColumn<>("Genre");
            genrecol.setMinWidth(200);
            genrecol.setCellValueFactory(new PropertyValueFactory<>("genrename"));

            TableColumn<database_object, String> languagecol = new TableColumn<>("Language");
            languagecol.setMinWidth(200);
            languagecol.setCellValueFactory(new PropertyValueFactory<>("songlanguage"));

            resultsTable.getColumns().clear();
            resultsTable.getColumns().addAll(songcol, albumcol, artistcol, genrecol, languagecol);
        } else if (search_from.equals("Playlists")) {

            MenuItem view_playlist = new MenuItem();
            view_playlist.setText("View");
            view_playlist.setOnAction(e -> {
                try {
                    view_playlist_table();
                } catch (IOException o) {
                    System.err.println(e);
                }
            });

            table_context.getItems().add(view_playlist);

            TableColumn<database_object, String> playlistcol = new TableColumn<>("Playlist");
            playlistcol.setMinWidth(200);
            playlistcol.setCellValueFactory(new PropertyValueFactory<>("playlistname"));

            TableColumn<database_object, String> usercol = new TableColumn<>("Username");
            usercol.setMinWidth(200);
            usercol.setCellValueFactory(new PropertyValueFactory<>("username"));

            resultsTable.getColumns().clear();
            resultsTable.getColumns().addAll(playlistcol, usercol);
        } else if (search_from.equals("Users")) {
            TableColumn<database_object, String> usercol = new TableColumn<>("Username");
            usercol.setMinWidth(200);
            usercol.setCellValueFactory(new PropertyValueFactory<>("username"));

            TableColumn<database_object, String> fullnamecol = new TableColumn<>("Full Name");
            fullnamecol.setMinWidth(200);
            fullnamecol.setCellValueFactory(new PropertyValueFactory<>("fullname"));

            TableColumn<database_object, Integer> agecol = new TableColumn<>("Age");
            agecol.setMinWidth(200);
            agecol.setCellValueFactory(new PropertyValueFactory<>("age"));

            TableColumn<database_object, String> countrycol = new TableColumn<>("Country");
            countrycol.setMinWidth(200);
            countrycol.setCellValueFactory(new PropertyValueFactory<>("country"));

            TableColumn<database_object, Boolean> admincol = new TableColumn<>("Admin?");
            admincol.setMinWidth(200);
            admincol.setCellValueFactory(new PropertyValueFactory<>("admin"));

            resultsTable.getColumns().clear();
            resultsTable.getColumns().addAll(usercol, fullnamecol, agecol, countrycol, admincol);
        }
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/andrew_yeh/Desktop/Code/NOT Spotify/src/application/playlist_organizer.db");
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys = ON");
            statement.setQueryTimeout(30);
            ResultSet rs = null;
            if (search_from.equals("Songs")) {
                rs = statement.executeQuery("select s_songID, s_name, al_name, ar_name, g_name, s_language from genres, songs, artists, albums where s_name like '" + search_term + "%' and s_albID=al_albID and s_artID=ar_artID and s_genID=g_genID order by s_name asc");
            } else if (search_from.equals("Artists")) {
                rs = statement.executeQuery("select ar_artID, s_name, al_name, ar_name, g_name, s_language from songs, albums, artists, genres where ar_name like '" + search_term + "%' and s_albID=al_albID and s_artID=ar_artID and s_genID=g_genID group by s_name order by ar_name asc");
            } else if (search_from.equals("Albums")) {
                rs = statement.executeQuery("select al_albID, s_name, al_name, ar_name, g_name, s_language from songs, albums, artists, genres where s_albID=al_albID and s_artID=ar_artID and s_genID=g_genID and al_name like '" + search_term + "%' group by s_name order by al_name asc");
            } else if (search_from.equals("Genres")) {
                rs = statement.executeQuery("select g_genID, s_name, al_name, ar_name, g_name, s_language from songs, albums, artists, genres where s_genID=g_genID and s_albID=al_albID and s_artID=ar_artID and g_name like '" + search_term + "%' group by s_name order by g_name asc");
            } else if (search_from.equals("Playlists")) {
                rs = statement.executeQuery("select p_playlistID, p_name, u_username from playlists, users where p_userID=u_userID and p_name like '" + search_term + "%'");
            } else if (search_from.equals("Users")) {
                search_select = "u_username";
                rs = statement.executeQuery("select u_userID, u_username, u_fullname, u_age, u_country, u_admin from users where u_username like '" + search_term + "%'");
            }
            while (rs.next()) {
                if (search_from.equals("Songs")) {
                    resultsList.add(new database_object(rs.getInt("s_songID"), rs.getString("s_name"), rs.getString("al_name"), rs.getString("ar_name"), rs.getString("g_name"), null, null, "songs", rs.getString("s_language")));
                } else if (search_from.equals("Albums")) {
                    resultsList.add(new database_object(rs.getInt("al_albID"), rs.getString("s_name"), rs.getString("al_name"), rs.getString("ar_name"), rs.getString("g_name"), null, null, "albums", rs.getString("s_language")));
                } else if (search_from.equals("Artists")) {
                    resultsList.add(new database_object(rs.getInt("ar_artID"), rs.getString("s_name"), rs.getString("al_name"), rs.getString("ar_name"), rs.getString("g_name"), null, null, "artists", rs.getString("s_language")));
                } else if (search_from.equals("Genres")) {
                    resultsList.add(new database_object(rs.getInt("g_genID"), rs.getString("s_name"), rs.getString("al_name"), rs.getString("ar_name"), rs.getString("g_name"), null, null, "genres", rs.getString("s_language")));
                } else if (search_from.equals("Playlists")) {
                    resultsList.add(new database_object(rs.getInt("p_playlistID"), null, null, null, null, rs.getString("p_name"), rs.getString("u_username"), "playlists", null));
                } else if (search_from.equals("Users")) {
                    database_object user = new database_object();
                    user.setUser(rs.getInt("u_userID"), rs.getString("u_username"), rs.getString("u_fullname"), rs.getInt("u_age"), rs.getString("u_country"), rs.getInt("u_admin"), "users");
                    resultsList.add(user);
                }
            }
            resultsTable.setItems(resultsList);
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
