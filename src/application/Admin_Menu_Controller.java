package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class Admin_Menu_Controller implements Initializable {

    public String search_from,search_select,search_term;
    public TextField search_input;
    public ListView<String> resultsList;
    public ChoiceBox<String> search_dropdown;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        search_dropdown.getItems().addAll("Songs", "Albums", "Artists", "Genres", "Playlists", "Users");
        search_dropdown.setValue("Songs");
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

    public void searchQuery()
    {
        search_term = search_input.getText().replace("'","''");
        search_from = search_dropdown.getValue();
        resultsList.getItems().clear();
        try
        {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/andrew_yeh/Desktop/Code/NOT Spotify/src/application/playlist_organizer.db");
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys = ON");
            statement.setQueryTimeout(30);
            ResultSet rs = null;
            if(search_from.equals("Songs"))
            {
                search_select = "s_name";
                rs = statement.executeQuery("select s_name, al_name, ar_name, g_name from songs, artists, genres, albums where "+search_select+" like '%"+search_term+"%' and s_artID=ar_artID and ar_genID=g_genID and s_albID=al_albID order by ar_name,al_name asc");
            }
            else if(search_from.equals("Artists"))
            {
                search_select = "ar_name";
                rs = statement.executeQuery("select ar_name, g_name from artists, genres where ar_genID=g_genID and ar_name like '"+search_term+"%' order by ar_name asc");
            }
            else if(search_from.equals("Albums"))
            {
                search_select = "al_name";
                rs = statement.executeQuery("select al_name, ar_name, g_name from albums, artists, genres where al_name like '"+search_term+"%' and al_artID=ar_artID and g_genID=ar_genID order by al_name asc");
            }
            else if(search_from.equals("Genres"))
            {
                search_select = "g_name";
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
                    resultsList.getItems().add("'" + rs.getString(search_select) + "' in "+ rs.getString("al_name") + " by " + rs.getString("ar_name") + " in " + rs.getString("g_name"));
                }
                else if(search_from.equals("Albums"))
                {
                    resultsList.getItems().add("'" + rs.getString(search_select) + "' by " + rs.getString("ar_name") + " in " + rs.getString("g_name"));
                }
                else if(search_from.equals("Artists"))
                {
                    resultsList.getItems().add(rs.getString(search_select) + " in " + rs.getString("g_name"));
                }
                else if(search_from.equals("Playlists"))
                {
                    resultsList.getItems().add(rs.getString(search_select) + " created by " + rs.getString("u_username"));
                }
                else
                {
                    resultsList.getItems().add(rs.getString(search_select));
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
