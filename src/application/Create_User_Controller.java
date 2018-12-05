package application;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Create_User_Controller implements Initializable {

    public TextField usernameField;
    public TextField fullnameField;
    public TextField ageField;
    public int age;
    public ChoiceBox<String> countryChoiceBox;
    public PasswordField passwordField;
    public PasswordField confirmpasswordField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Path path = Paths.get("/Users/andrew_yeh/Desktop/Code/NOT Spotify/src/application/countries.txt");
            List<String> countries = Files.lines(path).collect(Collectors.toList());
            countryChoiceBox.getItems().addAll(countries);
            countryChoiceBox.setValue("United States");
        }
        catch(IOException e) {
            System.err.println(e);
        }
    }

    public boolean isInt(TextField field) {
        try {
            age = Integer.parseInt(field.getText());
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    public void add_user(ActionEvent event) throws IOException {
        String username,fullname,country,password,confirmpassword;
        if(usernameField.getText().isEmpty() || fullnameField.getText().isEmpty() || ageField.getText().isEmpty() || passwordField.getText().isEmpty() || confirmpasswordField.getText().isEmpty()) {
            popup_windows.fill_all_fields_popup();
        } else {
            if(isInt(ageField)) {
                username = usernameField.getText();
                fullname = fullnameField.getText();
                country = countryChoiceBox.getValue();
                password = passwordField.getText();
                confirmpassword = confirmpasswordField.getText();
                if(password.equals(confirmpassword)) {
                    try {
                        Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/andrew_yeh/Desktop/Code/NOT Spotify/src/application/playlist_organizer.db");
                        Statement statement = connection.createStatement();
                        statement.execute("PRAGMA foreign_keys = ON");
                        statement.setQueryTimeout(30);
                        ResultSet rs = statement.executeQuery("select * from users where u_username='" + username + "'");
                        if (rs.isBeforeFirst()) {
                            popup_windows.user_exists_popup();
                        } else {
                            statement.executeUpdate("insert into users(u_username,u_fullname,u_age,u_country,u_admin,u_password) values ('" + username + "','" + fullname + "'," + age + ",'" + country + "'," + 0 + ",'" + password + "')");
                        }
                        connection.close();
                        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        window.close();
                    } catch (SQLException e) {
                        System.err.println(e.getMessage());
                    }
                } else {
                    popup_windows.no_match_popup();
                }
            }
            else {
                popup_windows.valid_age_popup();
            }
        }

    }

}
