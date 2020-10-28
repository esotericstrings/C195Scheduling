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

import utils.DBConnect;
import utils.Time;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
/**
 * Controller for appointment updating screen
 */
public class ModifyAppointmentController implements Initializable {
    /**
     * Loads resource bundle for localization/translation of text strings
     */
    private ResourceBundle rb = ResourceBundle.getBundle("language_files/rb");
    /**
     * Stores local user session data and appointment information from database
     */
    private final DataStore ds;
    /**
     * Appointment selected for modification
     */
    private final Appointment appointment;
    /**
     * Appointment modification form label
     */
    @FXML
    private Label appointmentTitle, contactLabel, typeLabel, startLabel, endLabel, userLabel, customerIdLabel, appointmentIdLabel, titleLabel, descriptionLabel, locationLabel, countryLabel;
    /**
     * Text input field to modify appointment information
     */
    @FXML
    private TextField appointmentIdField, titleField, descriptionField, locationField, customerIdField, contactField, typeField;
    /**
     * Datepicker input field to modify appointment information
     */
    @FXML
    private DatePicker startTimeField, endTimeField;
    /**
     * Button for appointment modification form
     */
    @FXML
    private Button saveButton, cancelButton;
    /**
     * Dropdown input field to modify appointment information
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
    /**
     * Selected customer
     */
    private String customer;
    /**
     * Selected contact
     */
    private String contact;
    /**
     * Selected user
     */
    private String user;


    /**
     *
     * @param ds Data store with local session data
     * @param appointment Selected appointment to be modified
     */
    public ModifyAppointmentController(DataStore ds, Appointment appointment) {
        this.ds = ds;
        this.appointment = appointment;
    }
    /**
     * Initializes screen with translated labels, default dropdown settings, and
     * loads appointment data into input fields for modification
     * @param url screen url
     * @param resourceBundle text localization resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadLabels();
        loadComboBoxes();
        loadTime();
        setData();
    }
    /**
     * Sets strings in user interface to translated text
     */
    @FXML
    private void loadLabels() {
        appointmentTitle.setText(rb.getString("modify_appointment"));
        appointmentIdLabel.setText(rb.getString("id"));
        appointmentIdField.setPromptText(rb.getString("disabled_auto_gen"));
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
        saveButton.setText(rb.getString("save"));
        cancelButton.setText(rb.getString("cancel"));
    }

    /**
     * Load dropdowns with available contact/customer/user options
     */
    @FXML
    private void loadComboBoxes() {
        String rq1 = "SELECT * FROM contacts";
        String rq2 = "SELECT * FROM customers";
        String rq3 = "SELECT * FROM users";
        try {
            Statement st1 = DBConnect.getConn().createStatement();
            Statement st2 = DBConnect.getConn().createStatement();
            Statement st3 = DBConnect.getConn().createStatement();
            ResultSet rs1 = st1.executeQuery(rq1);
            ResultSet rs2 = st2.executeQuery(rq2);
            ResultSet rs3 = st3.executeQuery(rq3);
            this.contactBoxField.getItems().clear();
            this.contacts.clear();
            this.customerBoxField.getItems().clear();
            this.customers.clear();
            this.userBoxField.getItems().clear();
            this.users.clear();
            while (rs1.next()) {
                String contact = rs1.getString("Contact_Name");
                contactBoxField.getItems().add(contact);
                this.contacts.add(contact);
                this.contact = contact ;
            }
            while (rs2.next()) {
                String customer = rs2.getString("Customer_Name");
                this.customerBoxField.getItems().add(customer);
                this.customers.add(customer);
                this.customer = customer;
            }
            while (rs3.next()) {
                String user = rs3.getString("User_Name");
                this.userBoxField.getItems().add(user);
                this.users.add(user);
                this.user = user;
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

    /**
     * Update available ending minute options based on appointment end hour set
     * @param event End hour dropdown change
     */
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
     * Sets input fields with existing appointment data for modification
     */
    private void setData() {
        appointmentIdField.setText(Integer.toString(appointment.getAppointmentId()));
        titleField.setText(appointment.getTitle());
        descriptionField.setText(appointment.getDescription());
        locationField.setText(appointment.getLocation());
        customerBoxField.setValue(this.customer);
        typeField.setText(appointment.getType());
        startTimeField.setValue(appointment.getLocalStart().toLocalDateTime().toLocalDate());
        startHour.setValue(Integer.toString(appointment.getLocalStart().toLocalDateTime().getHour()));
        startMin.setValue(Integer.toString(appointment.getLocalStart().toLocalDateTime().getMinute()));
        endTimeField.setValue(appointment.getLocalEnd().toLocalDateTime().toLocalDate());
        endHour.setValue(Integer.toString(appointment.getLocalEnd().toLocalDateTime().getHour()));
        endMin.setValue(Integer.toString(appointment.getLocalEnd().toLocalDateTime().getMinute()));
        userBoxField.setValue(this.user);
        contactBoxField.setValue(this.contact);
    }

    /**
     * Validates selected appointment time is within business hours and does not conflict with existing customer appointment.
     * Throws appropriate error alert if hours conflict.
     * @param utcStart Selected meeting start time in Timestamp format
     * @param utcEnd Selected meeting end time in Timestamp format
     * @param customerId Selected customer
     * @param appointmentId Selected appointment being validated
     * @return Boolean if selected appointment hours are valid or not
     */
    private boolean validateAppointment(Timestamp utcStart, Timestamp utcEnd, int customerId, int appointmentId) {
        ZonedDateTime UtcStart = ZonedDateTime.of(utcStart.toLocalDateTime(), ZoneId.of("UTC"));
        ZonedDateTime EstStart = UtcStart.withZoneSameInstant(ZoneId.of("America/New_York"));
        ZonedDateTime UtcEnd = ZonedDateTime.of(utcEnd.toLocalDateTime(), ZoneId.of("UTC"));
        ZonedDateTime EstEnd = UtcEnd.withZoneSameInstant(ZoneId.of("America/New_York"));

        try {
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
                if ( UTCCustomerStart.getDayOfYear() == UtcStart.getDayOfYear() && UtcStart.compareTo(UTCCustomerStart) < 1 && UtcEnd.compareTo(UTCCustomerEnd)  < 1) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(rb.getString("schedule_conflict"));
                    alert.setHeaderText(rb.getString("customer_schedule_conflict"));
                    alert.showAndWait();
                    return false;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        if (EstStart.getHour() > 22 || EstStart.getHour() < 8 || EstEnd.getHour() > 22 || EstEnd.getHour() < 8) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("business_hours"));
            alert.setHeaderText(rb.getString("business_hours_message"));
            alert.showAndWait();
            return false;
        }
        return true;
    }
    /**
     * Updates modification to appointment
     * @param event Button click event
     * @throws IOException Fails to load appointment management screen
     * @throws SQLException Fails to update appointment in database
     */
    @FXML
    private void handleSaveAppointmentButtonAction(ActionEvent event) throws IOException, SQLException {
        boolean valid = false;
        int newId = Integer.parseInt(appointmentIdField.getText());
        String titleFieldText = titleField.getText();
        String descriptionFieldText = descriptionField.getText();
        String locationFieldText = locationField.getText();
        String typeFieldText = typeField.getText();
        LocalDate startTime = startTimeField.getValue();
        String startHourValue = startHour.getValue();
        String startMinValue = startMin.getValue();
        LocalDate endTime = endTimeField.getValue();
        String endHourValue = endHour.getValue();
        String endMinValue = endMin.getValue();
        Timestamp current_time = new Timestamp( new java.sql.Date(System.currentTimeMillis()).getTime());
        String last_updated_by = ds.getUser();
        String contactFieldText = contactBoxField.getSelectionModel().getSelectedItem();
        String customerIdFieldText = customerBoxField.getSelectionModel().getSelectedItem();
        String userBoxFieldText = userBoxField.getSelectionModel().getSelectedItem();

        Time localTime = new Time();
        Timestamp utcStartTime = localTime.convertTimeToUTC(startTime, startHourValue, startMinValue);
        Timestamp utcEndTime = localTime.convertTimeToUTC(endTime, endHourValue, endMinValue);

        Timestamp localStartTimestamp = localTime.convertTimeToLocal(startTime, startHourValue, startMinValue);
        Timestamp localEndTimestamp = localTime.convertTimeToLocal(endTime, endHourValue, endMinValue);

            try {
                System.out.println("Connection is successful !!!!! Updating customers...");
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
                    String updateSql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Last_Update = ?, Last_Updated_By = ?, Start = ?, End = ?, Contact_ID = ?, Customer_ID = ?, User_ID = ? WHERE Appointment_ID = ?";
                    PreparedStatement myInsert = DBConnect.getConn().prepareStatement(updateSql);
                    myInsert.setString(1, titleFieldText);
                    myInsert.setString(2, descriptionFieldText);
                    myInsert.setString(3, locationFieldText);
                    myInsert.setString(4, typeFieldText);
                    myInsert.setTimestamp(5, current_time);
                    myInsert.setString(6, last_updated_by);
                    myInsert.setTimestamp(7, localStartTimestamp);
                    myInsert.setTimestamp(8, localEndTimestamp);
                    myInsert.setInt(9, contactId);
                    myInsert.setInt(10, customerId);
                    myInsert.setInt(11, userId);
                    myInsert.setInt(12, newId);
                    appointment.setName(titleFieldText);
                    appointment.setDescription(descriptionFieldText);
                    appointment.setLocation(locationFieldText);
                    appointment.setContactId(contactId);
                    appointment.setType(typeFieldText);
                    appointment.setCustomerId(customerId);
                    appointment.setUserId(userId);
                    appointment.setStart(utcStartTime);
                    appointment.setLocalStart(localStartTimestamp);
                    appointment.setFormattedLocalStart(localStartTimestamp);
                    appointment.setEnd(utcEndTime);
                    appointment.setLocalEnd(localEndTimestamp);
                    appointment.setFormattedLocalEnd(localEndTimestamp);
                    appointment.setLastUpdate(current_time);
                    appointment.setLastUpdatedBy(last_updated_by);
                    try (var ps = myInsert) {
                        ps.executeUpdate();
                    }
                    valid = true;
                }

            } catch(Exception e) {
                e.printStackTrace();
            }
            if (valid) {
                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/appointments.fxml"));
                Controller.AppointmentController controller = new Controller.AppointmentController(ds);
                loader.setController(controller);
                Scene scene = new Scene((Pane)loader.load());
                stage.setScene(scene);
                stage.show();
            }

    }
    /**
     * Exits appointment modification screen with
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
