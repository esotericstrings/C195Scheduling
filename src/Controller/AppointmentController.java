package Controller;

import Model.DataStore;
import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utils.DBConnect;

import java.time.LocalDateTime;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for appointment management screen
 */
public class AppointmentController implements Initializable {
    /**
     * Loads resource bundle for localization/translation of text strings
     */
    private ResourceBundle rb = ResourceBundle.getBundle("language_files/rb");
    /**
     * Stores local user session data and appointment information from database
     */
    private final DataStore ds;
    /**
     * Title for appointment screen
     */
    @FXML
    private Text appointmentTitle;
    /**
     * Appointment table view
     */
    @FXML
    private TableView<Appointment> appointmentTableView;
    /**
     * Number type table column for appointment data
     */
    @FXML
    private TableColumn<Appointment,Integer> appointmentId, contactId, customerId;
    /**
     * Text type table column for appointment data
     */
    @FXML
    private TableColumn<Appointment, String> title, description, customerAddress, startTime, endTime, location, contact, type;
    /**
     * Radio button for appointment time filtering
     */
    @FXML
    private RadioButton allAppointmentsRadioButton, appointmentWeekRadioButton, appointmentMonthRadioButton;
    /**
     * Button for navigating/filtering on appointment screen
     */
    @FXML
    private Button reportsButton, addAppointmentButton, updateAppointmentButton, deleteAppointmentButton, contactButton, customerButton, exitButton;
    /**
     * Toggle group for appointment time filters
     */
    private ToggleGroup RadioButtonToggleGroup;

    /**
     * Appointment Controller, saves local data store
     * @param ds Data store with local session data
     */
    public AppointmentController(DataStore ds) {
        this.ds = ds;
    }

    /**
     * Initializes screen with translated labels, default filter settings, and
     * loads appointment data into table
     * @param url screen url
     * @param resourceBundle text localization resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadLabels();
        //set data in table
        appointmentTableView.setItems(ds.getAllAppointments());
        //set all appointments radio button as default in toggle group
        RadioButtonToggleGroup = new ToggleGroup();
        allAppointmentsRadioButton.setToggleGroup(RadioButtonToggleGroup);
        appointmentWeekRadioButton.setToggleGroup(RadioButtonToggleGroup);
        appointmentMonthRadioButton.setToggleGroup(RadioButtonToggleGroup);
        allAppointmentsRadioButton.setSelected(true);
        appointmentWeekRadioButton.setSelected(false);
        appointmentMonthRadioButton.setSelected(false);
    }

    /**
     * Sets strings in user interface to translated text
     */
    private void loadLabels() {
        //set translated text to fields on string
        appointmentTitle.setText(rb.getString("appointments"));
        appointmentId.setText(rb.getString("id"));
        title.setText(rb.getString("title"));
        description.setText(rb.getString("description"));
        location.setText(rb.getString("location"));
        contact.setText(rb.getString("contact"));
        type.setText(rb.getString("type"));

        startTime.setText(rb.getString("start_time"));
        endTime.setText(rb.getString("end_time"));
        customerId.setText(rb.getString("customer_id"));

        addAppointmentButton.setText(rb.getString("add"));
        updateAppointmentButton.setText(rb.getString("update"));
        deleteAppointmentButton.setText(rb.getString("delete"));
        contactButton.setText(rb.getString("contacts"));
        customerButton.setText(rb.getString("customers"));
        exitButton.setText(rb.getString("exit"));
    }

    /**
     * Sets appointment table to show all appointments
     * @param event radio button click event
     */
    @FXML
    private void AllAppointmentsRadioButton(ActionEvent event) {
        //reset table filter to show all appointments
        appointmentTableView.setItems(ds.getAllAppointments());
    }

    /**
     * Filters appointment table to recent appointments within a week
     * @param event radio button click event
     */
    @FXML
    private void AppointmentWeekRadioButtonHandler(ActionEvent event) {
        //set table filter to show appointments within the current week
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlus1Week = now.plusWeeks(1);
        FilteredList<Appointment> filteredData = new FilteredList<>(ds.getAllAppointments());
        /**
         * Lambda filter on appointments to reduce code and allow for higher efficiency
         * in sorting through the appointment collection by allowing sequential/parallel processing
         */
        filteredData.setPredicate(row -> {
            LocalDateTime rowDate = row.getLocalStart().toLocalDateTime();
            return rowDate.isAfter(now.minusDays(1)) && rowDate.isBefore(nowPlus1Week);
        });
        appointmentTableView.setItems(filteredData);
    }

    /**
     * Filters appointment table to recent appointments within a month
     * @param event radio button click event
     */
    @FXML
    private void AppointmentMonthRadioButtonHandler(ActionEvent event) {
        //set table filter to show appointments within the current month
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlus1Month = now.plusMonths(1);
        FilteredList<Appointment> filteredData = new FilteredList<>(ds.getAllAppointments());
        /**
         * Lambda filter on appointments to reduce code and allow for higher efficiency
         * in sorting through the appointment collection by allowing sequential/parallel processing
         */
        filteredData.setPredicate(row -> {
            LocalDateTime rowDate = row.getLocalStart().toLocalDateTime();
            return rowDate.isAfter(now.minusDays(1)) && rowDate.isBefore(nowPlus1Month);
        });
        appointmentTableView.setItems(filteredData);
    }

    /**
     * Navigates to add appointment screen
     * @param event Button click event
     * @throws IOException Fails to load add appointment screen
     */
    @FXML
    private void handleAddAppointmentButtonAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/addappointment.fxml"));
        Controller.AddAppointmentController controller = new Controller.AddAppointmentController(ds);
        loader.setController(controller);
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Navigates to appointment modification and passes selected appointment contact to page
     * or triggers error alert if appointment is not selected
     * @param event Button click event
     * @throws IOException Fails to load appointment modification screen
     */
    @FXML
    private void handleModifyAppointmentButtonAction(ActionEvent event) throws IOException {
        //get user selected appointment from table
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/modifyappointment.fxml"));
            //pass selected appointment information to modify appointment controller
            Controller.ModifyAppointmentController controller = new Controller.ModifyAppointmentController(ds, selectedAppointment);
            loader.setController(controller);
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } else {
            //show error message if no appointment is selected for modification
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("error"));
            alert.setContentText(rb.getString("select_appointment"));
            alert.showAndWait();
        }
    }

    /**
     * Deletes selected appointment from database and local data store and triggers
     * appropriate alert for successful/failed deletion attempt
     * @param event Button click event
     * @throws SQLException Fails to delete selected appointment from database
     */
    @FXML
    private void handleDeleteAppointmentButtonAction(ActionEvent event) throws SQLException {
        if (appointmentTableView.getSelectionModel().getSelectedItem() != null) {
            ObservableList<Appointment> data = FXCollections.observableArrayList();
            data = appointmentTableView.getItems();
            // delete appointment by selected ID
            var deleteSql = "DELETE FROM appointments WHERE Appointment_ID = "+appointmentTableView.getSelectionModel().getSelectedItem().getAppointmentId();
            try (var ps = DBConnect.getConn().prepareStatement(deleteSql)) {
                ps.executeUpdate();
            }
            // delete selected appointment from local table
            data.remove(appointmentTableView.getSelectionModel().getSelectedItem());
            appointmentTableView.setItems(data);
            // show successful deletion message with details for deleted appointment
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("appointment_delete_title"));
            alert.setHeaderText(rb.getString("appointment_delete_title"));
            alert.setContentText(rb.getString("appointment_delete_title")+" #"+appointmentTableView.getSelectionModel().getSelectedItem().getAppointmentId()+", "+appointmentTableView.getSelectionModel().getSelectedItem().getType());
            alert.showAndWait();
        } else {
            //show error message if no appointment is selected for deletion
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("error"));
            alert.setContentText(rb.getString("appointment_not_found"));
            alert.showAndWait();
        }
    }

    /**
     * Navigates to reports screen
     * @param event button click event
     * @throws IOException Fails to load reports screen
     */
    @FXML
    private void handleReportsButtonAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/reports.fxml"));
        Controller.ReportsController controller = new Controller.ReportsController(ds);
        loader.setController(controller);
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }

    /**
     *  Navigates to contacts management screen
     * @param event button click event
     * @throws IOException Fails to load contacts screen
     */
    @FXML
    private void handleContactButtonAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/contacts.fxml"));
        Controller.ContactController controller = new Controller.ContactController(ds);
        loader.setController(controller);
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Navigates to customer management screen
     * @param event button click event
     * @throws IOException Fails to load customer screen
     */
    @FXML
    private void handleCustomerButtonAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/customers.fxml"));
        Controller.CustomerController controller = new Controller.CustomerController(ds);
        loader.setController(controller);
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Exits application with user confirmation
     * @param event Button click event
     */
    @FXML
    private void handleExitButtonAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(rb.getString("exit_title"));
        alert.setContentText(rb.getString("exit_message"));
        Optional<ButtonType> result = alert.showAndWait();
        //close window if user confirms exit
        if (result.get() == ButtonType.OK){
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }
}
