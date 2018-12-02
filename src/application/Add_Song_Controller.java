package application;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Add_Song_Controller implements Initializable {

    public ChoiceBox<String> albumList;
    public ChoiceBox<String> languageList;
    public TextField song_name_input;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try
        {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/andrew_yeh/Desktop/Code/NOT Spotify/src/application/playlist_organizer.db");
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys = ON");
            ResultSet rs = statement.executeQuery("select al_name from albums");
            albumList.setValue(rs.getString("al_name"));
            while(rs.next())
            {
                albumList.getItems().add(rs.getString("al_name"));
            }
            connection.close();
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage());
        }
        languageList.getItems().addAll("Afrikaans", "Albanian", "Amharic", "Arabic", "Armenian", "Azerbaijani", "Basque", "Belarusian", "Bengali", "Bosnian", "Bulgarian", "Catalan", "Cebuano", "Chinese (Simplified)", "Chinese (Traditional)", "Corsican", "Croatian", "Czech", "Danish", "Dutch", "English", "Esperanto", "Estonian", "Finnish", "French", "Frisian", "Galician", "Georgian", "German", "Greek", "Gujarati", "Haitian Creole", "Hausa", "Hawaiian", "Hebrew", "Hindi", "Hmong", "Hungarian", "Icelandic", "Igbo", "Indonesian", "Irish", "Italian", "Japanese", "Javanese", "Kannada", "Kazakh", "Khmer", "Korean", "Kurdish", "Kyrgyz", "Lao", "Latin", "Latvian", "Lithuanian", "Luxembourgish", "Macedonian", "Malagasy", "Malay", "Malayalam", "Maltese", "Maori", "Marathi", "Mongolian", "Myanmar (Burmese)", "Nepali", "Norwegian", "Nyanja (Chichewa)", "Pashto", "Persian", "Polish", "Portuguese (Portugal, Brazil)", "Punjabi", "Romanian", "Russian", "Samoan", "Scots Gaelic", "Serbian", "Sesotho", "Shona", "Sindhi", "Sinhala (Sinhalese)", "Slovak", "Slovenian", "Somali", "Spanish", "Sundanese", "Swahili", "Swedish", "Tagalog (Filipino)", "Tajik", "Tamil", "Telugu", "Thai", "Turkish", "Ukrainian", "Urdu", "Uzbek", "Vietnamese", "Welsh", "Xhosa", "Yiddish", "Yoruba", "Zulu");
        languageList.setValue("English");
    }

    public void add_song(ActionEvent event) throws IOException
    {
        try
        {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/andrew_yeh/Desktop/Code/NOT Spotify/src/application/playlist_organizer.db");
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys = ON");
            ResultSet rs = statement.executeQuery("select s_songID, al_albID from songs, albums where s_albID=al_albID and al_name like '"+albumList.getValue()+"'");
            if(rs.isBeforeFirst())
            {
                popup_windows.already_exists_popup();
            }
            else if(!song_name_input.getText().isEmpty())
            {
                String s_name = song_name_input.getText().replace("'","''");
                rs = statement.executeQuery("select al_albID,ar_artID,g_genID from albums,artists,genres where al_artID=ar_artID and al_genID=g_genID and al_name like '"+albumList.getValue()+"'");
                statement.executeUpdate("insert into songs(s_name, s_artID, s_albID, s_genID, s_language) values ('"+s_name+"',"+rs.getInt("ar_artID")+","+rs.getInt("al_albID")+","+rs.getInt("g_genID")+",'"+languageList.getValue()+"')");
                if(popup_windows.another_alert_popup())
                {
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.close();
                }
                else
                {
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.close();
                }
            }
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage());
        }
    }

}
