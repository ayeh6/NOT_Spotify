package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class Login_Screen_Controller {

    public TextField input_username;
    public PasswordField input_password;
    public int u_admin;
    public static int current_user_ID;
    public static String current_username;

    public void create_user(ActionEvent event) throws IOException
    {
        popup_windows.create_user_popup();
    }

    public void login_pressed(ActionEvent event) throws IOException
    {
        try
        {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/andrew_yeh/Desktop/Code/NOT Spotify/src/application/playlist_organizer.db");
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys = ON");
            statement.setQueryTimeout(30);
            String u_username = input_username.getText().replace("'","''");
            String u_password = input_password.getText().replace("'","''");
            ResultSet rs = statement.executeQuery("select u_username, u_userID, u_admin from users where u_username like '"+u_username+"' and u_password='"+u_password+"'");
            if(!rs.isBeforeFirst()) {
                popup_windows.invalid_user_password_popup();
            }
            else {
                current_user_ID = rs.getInt("u_userID");
                current_username = rs.getString("u_username");
                u_admin = rs.getInt("u_admin");
                if(u_admin==1)
                {
                    if(popup_windows.admin_alert_popup())
                    {
                        Parent admin_menuParent = FXMLLoader.load(getClass().getResource("admin_menu.fxml"));
                        Scene admin_menuScene = new Scene(admin_menuParent);
                        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                        window.setScene(admin_menuScene);
                        window.show();
                        connection.close();
                    }
                    else {
                        Parent admin_menuParent = FXMLLoader.load(getClass().getResource("user_menu.fxml"));
                        Scene admin_menuScene = new Scene(admin_menuParent);
                        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                        window.setScene(admin_menuScene);
                        window.show();
                        connection.close();
                    }
                }
                else if(u_admin==0)
                {
                    Parent admin_menuParent = FXMLLoader.load(getClass().getResource("user_menu.fxml"));
                    Scene admin_menuScene = new Scene(admin_menuParent);
                    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                    window.setScene(admin_menuScene);
                    window.show();
                    connection.close();
                }
            }
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
    }


}
