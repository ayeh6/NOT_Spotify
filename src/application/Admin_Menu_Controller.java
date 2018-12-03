package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Admin_Menu_Controller implements Initializable {

    public String search_from,search_select,search_term;
    public TextField search_input;
    public ListView<database_object> resultsList;
    public ChoiceBox<String> search_dropdown;
    public MenuBar menu_bar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        search_dropdown.getItems().addAll("Songs", "Albums", "Artists", "Genres", "Playlists", "Users");
        search_dropdown.setValue("Songs");
    }

    public void delete_song()
    {

    }

    public void add_song_menu() throws IOException
    {
        popup_windows.add_song_popup();
    }

    public void add_album_menu() throws IOException
    {
        popup_windows.add_album_popup();
    }

    public void add_artist_menu() throws IOException
    {
        popup_windows.add_artist_popup();
    }

    public void add_genre_menu() throws IOException
    {
        popup_windows.add_genre_popup();
    }

    public void log_out() throws IOException
    {
        Parent parent = FXMLLoader.load(getClass().getResource("login_screen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage)menu_bar.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void searchQuery()
    {
        search_term = search_input.getText().replace("'","''");
        search_from = search_dropdown.getValue();
        resultsList.getItems().clear();
        resultsList.setCellFactory(new Callback<ListView<database_object>, ListCell<database_object>>() {
            @Override
            public ListCell<database_object> call(ListView<database_object> database_objectListView) {
                return new ListCell<database_object>(){
                    @Override
                    protected void updateItem(database_object item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        if(empty || item == null || item.getName() == null)
                        {
                            setText(null);
                        }
                        else
                        {
                            setText(item.getName());
                        }
                    }
                };
            }
        });
        try
        {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/andrew_yeh/Desktop/Code/NOT Spotify/src/application/playlist_organizer.db");
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys = ON");
            statement.setQueryTimeout(30);
            ResultSet rs = null;
            if(search_from.equals("Songs"))
            {
                rs = statement.executeQuery("select distinct(s_songID), s_name from songs, artists, albums where s_name like '"+search_term+"%' and s_albID=al_albID and s_artID=ar_artID order by al_name, ar_name asc");
            }
            else if(search_from.equals("Artists"))
            {
                rs = statement.executeQuery("select ar_artID, ar_name from artists where ar_name like '"+search_term+"%' order by ar_name asc");
            }
            else if(search_from.equals("Albums"))
            {
                search_select = "al_name";
                rs = statement.executeQuery("select al_albID, al_name from albums where al_name like '"+search_term+"%' order by al_name asc");
            }
            else if(search_from.equals("Genres"))
            {
                rs = statement.executeQuery("select g_name from genres where g_name like '"+search_term+"%' order by g_name asc");
            }
            else if(search_from.equals("Playlists"))
            {
                search_select = "p_name";
                rs = statement.executeQuery("select p_name, u_username from playlists, users where p_userID=u_userID and p_name like '"+search_term+"%' order by p_name asc");
            }
            else if(search_from.equals("Users"))
            {
                search_select = "u_username";
                rs = statement.executeQuery("select u_username from users where u_username like '"+search_term+"%' order by u_username asc");
            }
            while(rs.next())
            {
                if(search_from.equals("Songs"))
                {
                    database_object result_item = new database_object(rs.getInt("s_songID"),rs.getString("s_name"),"songs");
                    resultsList.getItems().add(result_item);
                }
                else if(search_from.equals("Albums"))
                {
                    //resultsList.getItems().add("'" + rs.getString(search_select) + "' by " + rs.getString("ar_name") + " in " + rs.getString("g_name"));
                }
                else if(search_from.equals("Artists"))
                {
                    //resultsList.getItems().add(rs.getString(search_select) + " in " + rs.getString("g_name"));
                }
                else if(search_from.equals("Playlists"))
                {
                    //resultsList.getItems().add(rs.getString(search_select) + " created by " + rs.getString("u_username"));
                }
                else
                {
                    //resultsList.getItems().add(rs.getString(search_select));
                }
            }
            connection.close();
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage());
        }
    }
}
