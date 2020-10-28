package Controller;

import Main.Main;
import Model.Appointment;
import Model.DataStore;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utils.DBConnect;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for login screen
 */
public class LoginController implements Initializable {
    /**
     * Loads resource bundle for localization/translation of text strings
     */
    private ResourceBundle rb = ResourceBundle.getBundle("language_files/rb");
    /**
     * Stores local user session data and appointment information from database
     */
    private final DataStore ds;
    /**
     * Login form label
     */
    @FXML
    private Label loginTitleLabel, userNameLabel, passwordLabel, locationLabel, locationField;
    /**
     * Username of authorized user
     */
    @FXML
    private TextField userNameField;
    /**
     * Password of authorized user
     */
    @FXML
    private PasswordField passwordField;
    /**
     * Login form button
     */
    @FXML
    private Button loginButton, exitButton;

    /**
     * Controller for login page which inherits local user session data
     * @param ds local data store with session data
     */
    public LoginController(DataStore ds) {
        this.ds = ds;
    }

    /**
     * Initializes login screen with text localization resource bundle, sets text on screen to translated strings, and sets user location from system-detected setting
     * @param url screen url
     * @param resourceBundle text localization resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // load location from user system-detected settings
        locationField.setText(this.ds.getLocale().getDisplayCountry());
        //set translated text to fields on string
        loginTitleLabel.setText(rb.getString("login"));
        loginButton.setText(rb.getString("login"));
        exitButton.setText(rb.getString("exit"));
        userNameLabel.setText(rb.getString("username"));
        userNameField.setPromptText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        passwordField.setPromptText(rb.getString("password"));
        locationLabel.setText(rb.getString("location"));
    }

    /**
     * Checks for appointments scheduled in the next 15 minutes and shows appropriate alert with recent appointment information
     */
    private void appointmentAlert() {
        // set variables for current time and time in 15 minutes
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlus15Min = now.plusMinutes(15);
        // format date with 12 hour AM/PM time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd KK:mm:ss a");
        //filter appointments by start time within 15 minutes of current time
        FilteredList<Appointment> filteredData = new FilteredList<>(ds.getAllAppointments());
        filteredData.setPredicate(row -> {
            LocalDateTime rowDate = row.getLocalStart().toLocalDateTime();
            return rowDate.isAfter(now.minusMinutes(1)) && rowDate.isBefore(nowPlus15Min);
        });
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        //if there are appointments in the next 15 minutes, list the appointment id and start date/time in alert message
        if (!filteredData.isEmpty()) {
            String appointment_message = rb.getString("appointment_alert_message") + "\n";
            for (int i = 0; i < filteredData.size(); i++) {
                appointment_message += rb.getString("appointment") + " " + filteredData.get(i).getAppointmentId() + ": " + filteredData.get(i).getLocalStart().toLocalDateTime().format(formatter) + " \n";
            }
            alert.setTitle(rb.getString("appointment_alert_title"));
            alert.setHeaderText(rb.getString("appointment_alert_title"));
            alert.setContentText(appointment_message);
        } else {
            alert.setTitle(rb.getString("no_appointment_title"));
            alert.setHeaderText(rb.getString("no_appointment_title"));
            alert.setContentText(rb.getString("no_appointment_message")+ "\n");
        }

        alert.showAndWait();
    }

    /**
     * Checks login input against authorized users in database and logs activity to login_activity.txt
     * @param event Button click event
     * @throws IOException Failure to write login activity to login_activity.txt
     */
    @FXML
    private void loginAction(ActionEvent event) throws IOException {
        boolean valid = false;
        String userNameFieldText = userNameField.getText();
        String passwordFieldText = passwordField.getText();
        String login_activity_message = "";
        Timestamp current_time = new Timestamp( new java.sql.Date(System.currentTimeMillis()).getTime());
        try {
            String rq1 = "SELECT * FROM users WHERE User_Name = '"+userNameFieldText+"';";
            Statement st1 = DBConnect.getConn().createStatement();
            ResultSet rs1 = st1.executeQuery(rq1);
            while (rs1.next()) {
                String userID = rs1.getString("User_Name");
                String userPW = rs1.getString("Password");
                // if inputted user/pw matches a user/pw combo in database, allow login
                if (userID.matches(userNameFieldText) && passwordFieldText.matches(userPW)) {
                    valid = true;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        if (valid) {
            // show recent appointment alert and set valid login logging message
            appointmentAlert();
            login_activity_message = current_time+": LOGIN SUCCESS by "+userNameFieldText+"\n";
            // allow navigation to appointments after valid login
            ds.addUser(userNameFieldText);
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/appointments.fxml"));
            Controller.AppointmentController controller = new Controller.AppointmentController(ds);
            loader.setController(controller);
            Scene scene = new Scene((Pane)loader.load());
            stage.setScene(scene);
            stage.show();
        } else {
            // show login failure alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(rb.getString("invalid_login_title"));
            alert.setHeaderText(rb.getString("invalid_login_message"));
            alert.showAndWait();
            //set login failure message
            login_activity_message = current_time+": LOGIN FAIL by "+userNameFieldText+"\n";
        }
        // log login activity and time to login_activity.txt
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(
                    new File("login_activity.txt"), true ));
            pw.append(login_activity_message);
            pw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Exits application with user confirmation
     * @param event Button click event
     */
    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(rb.getString("exit_title"));
        alert.setContentText(rb.getString("exit_message"));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.NO){
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }
}
