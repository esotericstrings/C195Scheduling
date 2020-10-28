package Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import utils.Time;
import utils.DBConnect;

import java.time.format.DateTimeFormatter;
import java.util.*;
/**
 * Controller for new appointment screen
 */
public class AddAppointmentController implements Initializable {
    /**
     * Loads resource bundle for localization/translation of text strings
     */
    private ResourceBundle rb = ResourceBundle.getBundle("language_files/rb");
    /**
     * Stores local user session data and appointment information from database
     */
    private final DataStore ds;
    /**
     * Appointment creation form label
     */
    @FXML
    private Label appointmentTitle, contactLabel, typeLabel, startLabel, endLabel, userLabel, customerIdLabel, appointmentIdLabel, titleLabel, descriptionLabel, locationLabel, countryLabel;
    /**
     * Text input field to add new appointment information
     */
    @FXML
    private TextField appointmentIdField, titleField, descriptionField, locationField, customerIdField, contactField, typeField;
    /**
     * Datepicker input field to add new appointment information
     */
    @FXML
    private DatePicker startTimeField, endTimeField;
    /**
     * Button for appointment form
     */
    @FXML
    private Button createNewAppointmentButton, cancelButton;
    /**
     * Dropdown input field to add new appointment information
     */
    @FXML
    private ComboBox<String> contactBoxField, customerBoxField, userBoxField, startHour, startMin, endHour, endMin;
    /**
     * List of available customers
     */
    private List<String> customers = new ArrayList<>();
    /**
     * List of available contacts
     */
    private List<String> contacts = new ArrayList<>();
    /**
     * List of available users
     */
    private List<String> users = new ArrayList<>();

    public AddAppointmentController(DataStore ds) {
        this.ds = ds;
    }
    /**
     * Initializes screen with translated labels and default dropdown settings
     * @param url screen url
     * @param resourceBundle text localization resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadLabels();
        loadComboBoxes();
        loadTime();
    }
    /**
     * Sets strings in user interface to translated text
     */
    private void loadLabels() {
        //set translated text to fields on string
        appointmentTitle.setText(rb.getString("add_appointment"));
        appointmentIdLabel.setText(rb.getString("id"));
        appointmentIdField.setPromptText(rb.getString("disabled_auto_gen"));
        appointmentTitle.setText(rb.getString("add_customer"));
        titleLabel.setText(rb.getString("title"));
        titleField.setPromptText(rb.getString("title"));
        descriptionLabel.setText(rb.getString("description"));
        descriptionField.setPromptText(rb.getString("description"));
        locationLabel.setText(rb.getString("location"));
        locationField.setPromptText(rb.getString("location"));
        contactLabel.setText(rb.getString("contact"));
        typeLabel.setText(rb.getString("type"));
        typeField.setPromptText(rb.getString("type"));
        startLabel.setText(rb.getString("start_time"));
        endLabel.setText(rb.getString("end_time"));
        customerIdLabel.setText(rb.getString("customer_id"));
        createNewAppointmentButton.setText(rb.getString("save"));
        cancelButton.setText(rb.getString("cancel"));
    }
    /**
     * Load dropdowns with available contact/customer/user options
     */
    private void loadComboBoxes() {
        //clear all dropdowns
        this.contactBoxField.getItems().clear();
        this.contacts.clear();
        this.customerBoxField.getItems().clear();
        this.customers.clear();
        this.userBoxField.getItems().clear();
        this.users.clear();
        //load contact and customer dropdowns from datastore
        for (int i = 0; i < ds.getAllContacts().size(); i++) {
            String contact = ds.getAllContacts().get(i).getName();
            contactBoxField.getItems().add(contact);
            this.contacts.add(i, contact);
        }
        for (int i = 0; i < ds.getAllCustomers().size(); i++) {
            String customer = ds.getAllCustomers().get(i).getCustomerName();
            this.customerBoxField.getItems().add(customer);
            this.customers.add(customer);
        }
        try {
            //load user dropdown from db
            Statement st3 = DBConnect.getConn().createStatement();
            String rq3 = "SELECT * FROM users";
            ResultSet rs3 = st3.executeQuery(rq3);
            while (rs3.next()) {
                String user = rs3.getString("User_Name");
                this.userBoxField.getItems().add(user);
                this.users.add(user);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Load time dropdown with available hour/minute options
     */
    private void loadTime() {
        ObservableList<String> startHours = FXCollections.observableArrayList();
        ObservableList<String> endHours = FXCollections.observableArrayList();
        ObservableList<String> startMinutes = FXCollections.observableArrayList();
        ObservableList<String> endMinutes = FXCollections.observableArrayList();
        ZoneId estZone = ZoneId.of("America/New_York");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startTime = LocalDateTime.parse("2020-10-20 08:00", formatter);
        LocalDateTime endTime = LocalDateTime.parse("2020-10-20 22:00", formatter);
        ZonedDateTime EstStart = ZonedDateTime.of(startTime, estZone);
        ZonedDateTime EstEnd = ZonedDateTime.of(endTime, estZone);
        ZonedDateTime localStart = EstStart.withZoneSameInstant(ZoneId.systemDefault());
        ZonedDateTime localEnd = EstEnd.withZoneSameInstant(ZoneId.systemDefault());

        for (int i= localStart.getHour(); i <= localEnd.getHour(); i++) {
            if (i<10) {
                startHours.add("0"+Integer.toString(i));
                endHours.add("0"+Integer.toString(i));

            } else {
                endHours.add(Integer.toString(i));
                if (i < localEnd.getHour() && localEnd.getMinute() == 0) {
                    startHours.add(Integer.toString(i));
                }
            }
        }

        startMinutes.addAll("00", "15", "30", "45");
        endMinutes.addAll("00", "15", "30", "45");
        startHour.setItems(startHours);
        startMin.setItems(startMinutes);
        endHour.setItems(endHours);
        endMin.setItems(endMinutes);
    }
    @FXML
    private void updateDropdown(ActionEvent event) {
        ObservableList<String> endMinutes = FXCollections.observableArrayList();
        ObservableList<String> startMinutes = FXCollections.observableArrayList();
        endMin.getItems().clear();
        String endHourValue = endHour.getValue();
        ZoneId estZone = ZoneId.of("America/New_York");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startTime = LocalDateTime.parse("2020-10-20 08:00", formatter);
        LocalDateTime endTime = LocalDateTime.parse("2020-10-20 22:00", formatter);
        ZonedDateTime EstStart = ZonedDateTime.of(startTime, estZone);
        ZonedDateTime EstEnd = ZonedDateTime.of(endTime, estZone);
        ZonedDateTime localStart = EstStart.withZoneSameInstant(ZoneId.systemDefault());
        ZonedDateTime localEnd = EstEnd.withZoneSameInstant(ZoneId.systemDefault());
        if (Integer.parseInt(endHourValue) == localEnd.getHour() && localEnd.getMinute() == 0) {
            endMinutes.addAll("00");
        } else if (Integer.parseInt(endHourValue) == localStart.getHour()  && localStart.getMinute() == 0) {
            endMinutes.addAll("15", "30", "45");
        }
        else {
            endMinutes.addAll("00", "15", "30", "45");
        }
        startMinutes.addAll("00", "15", "30", "45");
        endMin.setItems(endMinutes);
        startMin.setItems(startMinutes);
    }
    /**
     * Validates selected appointment time is within business hours and does not conflict with existing customer appointment.
     * Throws appropriate error alert if hours conflict.
     * @param utcStart Selected meeting start time in Timestamp format
     * @param utcEnd Selected meeting end time in Timestamp format
     * @param customerId Selected customer
     * @param appointmentId Selected appointment being validated
     * @throws SQLException Fails to fetch customer appointment information from database
     * @return Boolean if selected appointment hours are valid or not
     */
    private boolean validateAppointment(Timestamp utcStart, Timestamp utcEnd, int customerId, int appointmentId) throws SQLException {
        ZonedDateTime UtcStart = ZonedDateTime.of(utcStart.toLocalDateTime(), ZoneId.of("UTC"));
        ZonedDateTime EstStart = UtcStart.withZoneSameInstant(ZoneId.of("America/New_York"));
        ZonedDateTime UtcEnd = ZonedDateTime.of(utcEnd.toLocalDateTime(), ZoneId.of("UTC"));
        ZonedDateTime EstEnd = UtcEnd.withZoneSameInstant(ZoneId.of("America/New_York"));

        // Check that proposed meeting hours are within business hours (8AM-10PM EST)
        if (EstStart.getHour() > 22 || EstStart.getHour() < 8 || EstEnd.getHour() > 22 || EstEnd.getHour() < 8) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("business_hours"));
            alert.setHeaderText(rb.getString("business_hours_message"));
            alert.showAndWait();
            return false;
        }

        // Check that proposed meeting hours do not conflict with customer's existing appointments
        String sq1 = "SELECT * FROM appointments WHERE Customer_ID = '"+customerId+"' AND Appointment_ID != '"+appointmentId+"';";
        Statement st1 = DBConnect.getConn().createStatement();
        ResultSet qrs1 = st1.executeQuery(sq1);
        String start = "";
        String end = "";
        while (qrs1.next()) {
            start = qrs1.getString("Start");
            end = qrs1.getString("End");
            ZonedDateTime UTCCustomerStart = ZonedDateTime.of(Timestamp.valueOf(start).toLocalDateTime(), ZoneId.of("UTC"));
            ZonedDateTime UTCCustomerEnd = ZonedDateTime.of(Timestamp.valueOf(end).toLocalDateTime(), ZoneId.of("UTC"));
            if ( UtcStart.compareTo(UTCCustomerStart) < 1 && UtcEnd.compareTo(UTCCustomerEnd)  < 1) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rb.getString("schedule_conflict"));
                alert.setHeaderText(rb.getString("customer_schedule_conflict"));
                alert.showAndWait();
                return false;
            }
        }
        return true;
    }
    /**
     * Adds new appointment
     * @param event Button click event
     * @throws IOException Fails to load appointment management screen
     * @throws SQLException Fails to fetch contact/user/customer information from database
     */
    @FXML
    private void createNewAppointmentAction(ActionEvent event) throws IOException, SQLException {
        boolean valid = false;
        int newId = ds.getAllAppointments().get(ds.getAllAppointments().size() - 1).getAppointmentId()+1;
        String titleFieldText = titleField.getText();
        String descriptionFieldText = descriptionField.getText();
        String locationFieldText = locationField.getText();
        String contactFieldText = contactBoxField.getSelectionModel().getSelectedItem();
        String typeFieldText = typeField.getText();
        LocalDate startTime = startTimeField.getValue();
        String startHourValue = startHour.getValue();
        String startMinValue = startMin.getValue();
        LocalDate endTime = endTimeField.getValue();
        String endHourValue = endHour.getValue();
        String endMinValue = endMin.getValue();

        Time localTime = new Time();
        Timestamp utcStartTime = localTime.convertTimeToUTC(startTime, startHourValue, startMinValue);
        Timestamp utcEndTime = localTime.convertTimeToUTC(endTime, endHourValue, endMinValue);
        Timestamp localStartTimestamp = localTime.convertTimeToLocal(startTime, startHourValue, startMinValue);
        Timestamp localEndTimestamp = localTime.convertTimeToLocal(endTime, endHourValue, endMinValue);

        Timestamp current_date = new Timestamp( new java.sql.Date(System.currentTimeMillis()).getTime());
        String created_by = ds.getUser();
        String last_updated_by = ds.getUser();
        String customerIdFieldText = customerBoxField.getSelectionModel().getSelectedItem();
        String userBoxFieldText = userBoxField.getSelectionModel().getSelectedItem();
        String sq1 = "SELECT * FROM contacts WHERE Contact_Name = '"+contactFieldText+"';";
        Statement st1 = DBConnect.getConn().createStatement();
        ResultSet qrs1 = st1.executeQuery(sq1);
        int contactId = 0;
        while (qrs1.next()) {
            contactId = qrs1.getInt("Contact_ID");
        }
        String sq2 = "SELECT * FROM customers WHERE Customer_Name = '"+customerIdFieldText+"';";
        st1 = DBConnect.getConn().createStatement();
        ResultSet qrs2 = st1.executeQuery(sq2);
        int customerId = 0;
        while (qrs2.next()) {
            customerId = qrs2.getInt("Customer_ID");
        }
        String sq3 = "SELECT * FROM users WHERE User_Name = '"+userBoxFieldText+"';";
        st1 = DBConnect.getConn().createStatement();
        ResultSet qrs3 = st1.executeQuery(sq3);
        int userId = 0;
        while (qrs3.next()) {
            userId = qrs3.getInt("User_ID");
        }
        if (validateAppointment(utcStartTime, utcEndTime, customerId, newId)) {
            System.out.println("Connection is successful !!!!! Adding appointment...");
            String rq2 = "INSERT INTO appointments VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement myInsert = DBConnect.getConn().prepareStatement(rq2);
            myInsert.setInt(1, newId);
            myInsert.setString(2, titleFieldText);
            myInsert.setString(3, descriptionFieldText);
            myInsert.setString(4, locationFieldText);
            myInsert.setString(5, typeFieldText);
            myInsert.setTimestamp(6, localStartTimestamp);
            myInsert.setTimestamp(7, localEndTimestamp);
            myInsert.setTimestamp(8, current_date);
            myInsert.setString(9, created_by);
            myInsert.setTimestamp(10, current_date);
            myInsert.setString(11, last_updated_by);
            myInsert.setInt(12, customerId);
            myInsert.setInt(13, 1);
            myInsert.setInt(14, contactId);
            try (var ps = myInsert) {
                ps.executeUpdate();
                System.out.println("Appointment added...");
            }
            Appointment c1 = new Appointment(newId, titleFieldText, descriptionFieldText, locationFieldText, typeFieldText, contactId, utcStartTime, utcEndTime, current_date, "created_by", current_date, "last_updated_by", customerId, userId);
            c1.setLocalStart(localStartTimestamp);
            c1.setLocalEnd(localEndTimestamp);
            c1.setFormattedLocalStart(localStartTimestamp);
            c1.setFormattedLocalStart(localEndTimestamp);
            ds.addAppointment(c1);
            valid = true;
        }
        if (valid) {
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/appointments.fxml"));
            Controller.AppointmentController controller = new Controller.AppointmentController(ds);
            loader.setController(controller);
            Scene scene = new Scene((Pane)loader.load());
            stage.setScene(scene);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("error"));
            alert.showAndWait();
        }
    }
    /**
     * Exits add appointment screen with
     * user confirmation that changes will not save
     * @param event Button click event
     * @throws IOException Fails to load appointment screen
     */
    @FXML
    private void handleCancelButtonAction(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(rb.getString("cancel_confirmation"));
        alert.setContentText(rb.getString("cancel_appointment_message"));
        Optional<ButtonType> result = alert.showAndWait();
        //return to appointments without saving if user confirms exit
        if (result.get() == ButtonType.OK){
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/appointments.fxml"));
            Controller.AppointmentController controller = new Controller.AppointmentController(ds);
            loader.setController(controller);
            Scene scene = new Scene((Pane)loader.load());
            stage.setScene(scene);
            stage.show();
        }
    }
}
