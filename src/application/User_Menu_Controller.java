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

public class User_Menu_Controller implements Initializable {

    public String search_from,search_term;
    public TextField search_input;
    public TableView<database_object> resultsTable;
    public ListView<database_object> user_playlists;
    public ChoiceBox<String> search_dropdown;
    public ChoiceBox<String> sort_dropdown;
    public MenuBar menu_bar;
    public Menu user_menu;
    public MenuItem add_song;
    public ContextMenu table_context_menu;
    public static database_object selected_playlist;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        search_dropdown.getItems().addAll("Songs", "Albums", "Artists", "Genres", "Playlists");
        search_dropdown.setValue("Songs");
        sort_dropdown.getItems().addAll("-", "Album", "Artist", "Genre", "Language");
        sort_dropdown.setValue("-");
        user_menu.setText(Login_Screen_Controller.current_username);
        add_song = new MenuItem();
        add_song.setText("add to playlist");
        table_context_menu.getItems().add(add_song);
        search_dropdown.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> {
            setSortDrop();
            searchQuery();
        });
        sort_dropdown.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> {
            searchQuery();
        });
        user_playlists.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(database_object item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getPlaylistname() == null) {
                    setText(null);
                }
                else {
                    setText(item.getPlaylistname());
                }
            }
        });

        //initialize list with playlist names
        list_playlists();
        searchQuery();

    }

    public void create_playlist() throws IOException{
        popup_windows.create_playlist_popup();
        list_playlists();
    }

    public void list_playlists()
    {
        ObservableList<database_object> resultsList = FXCollections.observableArrayList();
        if(!user_playlists.getItems().isEmpty()) {
            user_playlists.getItems().clear();
        }
        try{
            Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/andrew_yeh/Desktop/Code/NOT Spotify/src/application/playlist_organizer.db");
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys = ON");
            statement.setQueryTimeout(30);
            ResultSet rs = statement.executeQuery("select p_playlistID, p_name, u_username from playlists, users where u_userID=p_userID and p_userID="+Login_Screen_Controller.current_user_ID);
            while(rs.next())
            {
                resultsList.add(new database_object(rs.getInt("p_playlistID"), null, null, null, null, rs.getString("p_name"), rs.getString("u_username"), "playlists", null));
            }
            user_playlists.getItems().addAll(resultsList);
            connection.close();
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void delete_playlist()
    {
        database_object delete_item = user_playlists.getSelectionModel().getSelectedItem();
        if(delete_item!=null) {
            try {
                Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/andrew_yeh/Desktop/Code/NOT Spotify/src/application/playlist_organizer.db");
                Statement statement = connection.createStatement();
                statement.execute("PRAGMA foreign_keys = ON");
                statement.setQueryTimeout(30);
                statement.executeUpdate("delete from playlists where p_playlistID=" + delete_item.getID());
                connection.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
            list_playlists();
        }
    }

    public void change_password_menu() throws IOException
    {
        popup_windows.change_password_popup();
    }

    public void log_out() throws IOException
    {
        Parent parent = FXMLLoader.load(getClass().getResource("login_screen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage)menu_bar.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void setSortDrop()
    {
        sort_dropdown.getItems().clear();
        table_context_menu.getItems().clear();
        if(search_dropdown.getValue().equals("Songs")) {
            table_context_menu.getItems().add(add_song);
            sort_dropdown.getItems().addAll("-", "Album", "Artist", "Genre", "Language");
            sort_dropdown.setValue("-");
        }
        else if(search_dropdown.getValue().equals("Albums")) {
            sort_dropdown.getItems().addAll("-", "Artist", "Genre");
            sort_dropdown.setValue("-");
        }
        else if(search_dropdown.getValue().equals("Artists")) {
            sort_dropdown.getItems().addAll("-", "Genre");
            sort_dropdown.setValue("-");
        }
        else if(search_dropdown.getValue().equals("Genres")) {
            sort_dropdown.getItems().addAll("-");
            sort_dropdown.setValue("-");
        }
        else if(search_dropdown.getValue().equals("Playlists")) {
            sort_dropdown.getItems().addAll("-", "User");
            sort_dropdown.setValue("-");
        }
    }

    public void searchQuery() {
        String sort_by=null;
        search_term = search_input.getText().replace("'","''");;
        search_from = search_dropdown.getValue();
        ObservableList<database_object> resultsList = FXCollections.observableArrayList();
        if(!sort_dropdown.getItems().isEmpty()) {
            if (search_from.equals("Songs")) {
                if (sort_dropdown.getValue().equals("Song") || sort_dropdown.getValue().equals("-")) {
                    sort_by = "s_name";
                } else if (sort_dropdown.getValue().equals("Album")) {
                    sort_by = "al_name";
                } else if (sort_dropdown.getValue().equals("Artist")) {
                    sort_by = "ar_name";
                } else if (sort_dropdown.getValue().equals("Genre")) {
                    sort_by = "g_name";
                } else if (sort_dropdown.getValue().equals("Language")) {
                    sort_by = "s_language";
                }

                TableColumn<database_object, String> songcol = new TableColumn<>("Song");
                songcol.setMinWidth(200);
                songcol.setCellValueFactory(new PropertyValueFactory<>("songname"));
                songcol.setSortable(false);

                TableColumn<database_object, String> albumcol = new TableColumn<>("Album");
                albumcol.setMinWidth(200);
                albumcol.setCellValueFactory(new PropertyValueFactory<>("albumname"));
                albumcol.setSortable(false);

                TableColumn<database_object, String> artistcol = new TableColumn<>("Artist");
                artistcol.setMinWidth(200);
                artistcol.setCellValueFactory(new PropertyValueFactory<>("artistname"));
                artistcol.setSortable(false);

                TableColumn<database_object, String> genrecol = new TableColumn<>("Genre");
                genrecol.setMinWidth(200);
                genrecol.setCellValueFactory(new PropertyValueFactory<>("genrename"));
                genrecol.setSortable(false);

                TableColumn<database_object, String> languagecol = new TableColumn<>("Language");
                languagecol.setMinWidth(200);
                languagecol.setCellValueFactory(new PropertyValueFactory<>("songlanguage"));
                languagecol.setSortable(false);

                resultsTable.getColumns().clear();
                resultsTable.getColumns().addAll(songcol, albumcol, artistcol, genrecol, languagecol);
            }
            else if (search_from.equals("Albums")) {
                if (sort_dropdown.getValue().equals("Album") || sort_dropdown.getValue().equals("-")) {
                    sort_by = "al_name";
                }
                else if (sort_dropdown.getValue().equals("Artist")) {
                    sort_by = "ar_name";
                }
                else if (sort_dropdown.getValue().equals("Genre")) {
                    sort_by = "g_name";
                }

                TableColumn<database_object, String> albumcol = new TableColumn<>("Album");
                albumcol.setMinWidth(200);
                albumcol.setCellValueFactory(new PropertyValueFactory<>("albumname"));
                albumcol.setSortable(false);

                TableColumn<database_object, String> artistcol = new TableColumn<>("Artist");
                artistcol.setMinWidth(200);
                artistcol.setCellValueFactory(new PropertyValueFactory<>("artistname"));
                artistcol.setSortable(false);

                TableColumn<database_object, String> genrecol = new TableColumn<>("Genre");
                genrecol.setMinWidth(200);
                genrecol.setCellValueFactory(new PropertyValueFactory<>("genrename"));
                genrecol.setSortable(false);

                resultsTable.getColumns().clear();
                resultsTable.getColumns().addAll(albumcol, artistcol, genrecol);
            }
            else if (search_from.equals("Artists")) {
                if (sort_dropdown.getValue().equals("Artist") || sort_dropdown.getValue().equals("-")) {
                    sort_by = "ar_name";
                }
                else if (sort_dropdown.getValue().equals("Genre")) {
                    sort_by = "g_name";
                }

                TableColumn<database_object, String> artistcol = new TableColumn<>("Artist");
                artistcol.setMinWidth(200);
                artistcol.setCellValueFactory(new PropertyValueFactory<>("artistname"));
                artistcol.setSortable(false);

                TableColumn<database_object, String> genrecol = new TableColumn<>("Genre");
                genrecol.setMinWidth(200);
                genrecol.setCellValueFactory(new PropertyValueFactory<>("genrename"));
                genrecol.setSortable(false);

                resultsTable.getColumns().clear();
                resultsTable.getColumns().addAll(artistcol, genrecol);
            }
            else if (search_from.equals("Genres")) {
                TableColumn<database_object, String> genrecol = new TableColumn<>("Genre");
                genrecol.setMinWidth(200);
                genrecol.setCellValueFactory(new PropertyValueFactory<>("genrename"));
                genrecol.setSortable(false);

                resultsTable.getColumns().clear();
                resultsTable.getColumns().add(genrecol);
            }
            else if (search_from.equals("Playlists")) {
                if (sort_dropdown.getValue().equals("Playlist") || sort_dropdown.getValue().equals("-")) {
                    sort_by = "p_name";
                }
                else if (sort_dropdown.getValue().equals("User")) {
                    sort_by = "u_username";
                }

                TableColumn<database_object, String> playlistcol = new TableColumn<>("Playlist");
                playlistcol.setMinWidth(200);
                playlistcol.setCellValueFactory(new PropertyValueFactory<>("playlistname"));
                playlistcol.setSortable(false);

                TableColumn<database_object, String> usercol = new TableColumn<>("User");
                usercol.setMinWidth(200);
                usercol.setCellValueFactory(new PropertyValueFactory<>("username"));
                usercol.setSortable(false);

                resultsTable.getColumns().clear();
                resultsTable.getColumns().addAll(playlistcol, usercol);
            }
            try {
                Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/andrew_yeh/Desktop/Code/NOT Spotify/src/application/playlist_organizer.db");
                Statement statement = connection.createStatement();
                statement.execute("PRAGMA foreign_keys = ON");
                statement.setQueryTimeout(30);
                ResultSet rs = null;
                if (search_from.equals("Songs")) {
                    rs = statement.executeQuery("select s_songID, s_name, al_name, ar_name, g_name, s_language from genres, songs, artists, albums where s_name like '" + search_term + "%' and s_albID=al_albID and s_artID=ar_artID and s_genID=g_genID order by " + sort_by + " asc");
                }
                else if (search_from.equals("Artists")) {
                    rs = statement.executeQuery("select ar_artID, ar_name, g_name from artists, genres where g_genID=ar_genID and ar_name like '" + search_term + "%' order by " + sort_by + " asc");
                }
                else if (search_from.equals("Albums")) {
                    rs = statement.executeQuery("select al_albID, al_name, ar_name, g_name from albums, artists, genres where al_artID=ar_artID and al_genID=g_genID and al_name like '" + search_term + "%' order by " + sort_by + " asc");
                }
                else if (search_from.equals("Genres")) {
                    rs = statement.executeQuery("select g_genID, g_name from genres where g_name like '" + search_term + "%' order by g_name asc");
                }
                else if (search_from.equals("Playlists")) {
                    rs = statement.executeQuery("select p_playlistID, p_name, u_username from playlists, users where p_userID=u_userID and u_userID<>" + Login_Screen_Controller.current_user_ID + " and p_name like '" + search_term + "%' order by " + sort_by + " asc");
                }
                while (rs.next()) {
                    if (search_from.equals("Songs")) {
                        resultsList.add(new database_object(rs.getInt("s_songID"), rs.getString("s_name"), rs.getString("al_name"), rs.getString("ar_name"), rs.getString("g_name"), null, null, "songs", rs.getString("s_language")));
                    }
                    else if (search_from.equals("Albums")) {
                        resultsList.add(new database_object(rs.getInt("al_albID"), null, rs.getString("al_name"), rs.getString("ar_name"), rs.getString("g_name"), null, null, "albums", null));
                    }
                    else if (search_from.equals("Artists")) {
                        resultsList.add(new database_object(rs.getInt("ar_artID"), null, null, rs.getString("ar_name"), rs.getString("g_name"), null, null, "artists", null));
                    }
                    else if (search_from.equals("Genres")) {
                        resultsList.add(new database_object(rs.getInt("g_genID"), null, null, null, rs.getString("g_name"), null, null, "genres", null));
                    }
                    else if (search_from.equals("Playlists")) {
                        resultsList.add(new database_object(rs.getInt("p_playlistID"), null, null, null, null, rs.getString("p_name"), rs.getString("u_username"), "playlists", null));
                    }
                }
                resultsTable.setItems(resultsList);
                connection.close();
            }
            catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

}
