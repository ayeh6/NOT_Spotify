package application;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class Change_Password_Controller {

    public PasswordField current_passwordField;
    public PasswordField new_passwordField;
    public PasswordField confirm_passwordField;

    public void change_password(ActionEvent event) throws IOException {
        String current_password = current_passwordField.getText().replace("'", "''");
        String new_password = new_passwordField.getText().replace("'", "''");
        String confirm_password = confirm_passwordField.getText().replace("'", "''");
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/andrew_yeh/Desktop/Code/NOT Spotify/src/application/playlist_organizer.db");
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys = ON");
            statement.setQueryTimeout(30);
            ResultSet rs = statement.executeQuery("select u_userID from users where u_username like '" + Login_Screen_Controller.current_username + "' and u_password like '" + current_password + "'");
            if (!rs.isBeforeFirst()) {
                //invalid current password
                popup_windows.invalid_password_popup();
            } else {
                if (new_password.equals(confirm_password)) {
                    //change password
                    statement.executeUpdate("update users set u_password='" + new_password + "' where u_userID=" + Login_Screen_Controller.current_user_ID);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.close();
                } else {
                    //passwords do not match
                    popup_windows.no_match_popup();
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
