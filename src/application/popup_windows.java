package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class popup_windows {

    public static boolean admin_alert_popup() throws IOException
    {
        Parent admin_alertParent = FXMLLoader.load(popup_windows.class.getResource("admin_alert.fxml"));
        Scene admin_alertScene = new Scene(admin_alertParent);
        Stage admin_alertStage = new Stage();
        admin_alertStage.initModality(Modality.APPLICATION_MODAL);
        admin_alertStage.setScene(admin_alertScene);
        admin_alertStage.showAndWait();
        return Admin_Alert_Controller.yes;
    }

    public static boolean another_alert_popup() throws IOException
    {
        Parent parent = FXMLLoader.load(popup_windows.class.getResource("another_alert.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
        return Another_Controller.yes;
    }

    public static void already_exists_popup() throws IOException
    {
        Parent parent = FXMLLoader.load(popup_windows.class.getResource("already_exists.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public static void create_user_popup() throws IOException
    {
        Parent parent = FXMLLoader.load(popup_windows.class.getResource("create_user.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public static void fill_all_fields_popup() throws IOException
    {
        Parent parent = FXMLLoader.load(popup_windows.class.getResource("fill_all_fields.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public static void user_exists_popup() throws IOException
    {
        Parent parent = FXMLLoader.load(popup_windows.class.getResource("user_exists.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public static void invalid_user_password_popup() throws IOException
    {
        Parent parent = FXMLLoader.load(popup_windows.class.getResource("invalid_user_password.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public static void invalid_password_popup() throws IOException
    {
        Parent parent = FXMLLoader.load(popup_windows.class.getResource("invalid_password.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public static void no_match_popup() throws IOException
    {
        Parent parent = FXMLLoader.load(popup_windows.class.getResource("no_match.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public static void valid_age_popup() throws IOException
    {
        Parent parent = FXMLLoader.load(popup_windows.class.getResource("valid_age.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public static void add_artist_popup() throws IOException
    {
        Parent add_artistParent = FXMLLoader.load(popup_windows.class.getResource("add_artist.fxml"));
        Scene add_artistScene = new Scene(add_artistParent);
        Stage add_artistStage = new Stage();
        add_artistStage.initModality(Modality.APPLICATION_MODAL);
        add_artistStage.setScene(add_artistScene);
        add_artistStage.showAndWait();
    }

    public static void add_album_popup() throws IOException
    {
        Parent add_albumParent = FXMLLoader.load(popup_windows.class.getResource("add_album.fxml"));
        Scene add_albumScene = new Scene(add_albumParent);
        Stage add_albumStage = new Stage();
        add_albumStage.setScene(add_albumScene);
        add_albumStage.showAndWait();
    }

    public static void add_song_popup() throws IOException
    {
        Parent parent = FXMLLoader.load(popup_windows.class.getResource("add_song.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.showAndWait();
    }

    public static void add_genre_popup() throws IOException
    {
        Parent parent = FXMLLoader.load(popup_windows.class.getResource("add_genre.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.showAndWait();
    }

    public static void change_password_popup() throws IOException
    {
        Parent parent = FXMLLoader.load(popup_windows.class.getResource("change_password.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public static void create_playlist_popup() throws IOException
    {
        Parent parent = FXMLLoader.load(popup_windows.class.getResource("create_playlist.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public static boolean ask_add_song_playlist_popup() throws IOException
    {
        Parent parent = FXMLLoader.load(popup_windows.class.getResource("ask_add_song_playlist.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
        return Ask_Add_Song_Playlist_Controller.yes;
    }

    public static boolean add_song_playlist_popup() throws IOException
    {
        Parent parent = FXMLLoader.load(popup_windows.class.getResource("edit_playlist.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.showAndWait();
        return Ask_Add_Song_Playlist_Controller.yes;
    }

    public static void view_playlist_songs_popup() throws IOException
    {
        Parent parent = FXMLLoader.load(popup_windows.class.getResource("view_playlist_songs.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.showAndWait();
    }
}
