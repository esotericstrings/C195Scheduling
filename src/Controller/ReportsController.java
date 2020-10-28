package Controller;

import Model.DataStore;
import Model.Appointment;
import Model.Report;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
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
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.format.TextStyle;
import java.util.*;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

/**
 * Controller for reports screen
 */
public class ReportsController implements Initializable {
    /**
     * Loads resource bundle for localization/translation of text strings
     */
    private ResourceBundle rb = ResourceBundle.getBundle("language_files/rb");
    /**
     * Stores local user session data and appointment information from database
     */
    private final DataStore ds;
    /**
     * Title for reports screen
     */
    @FXML
    private Text appointmentTitle;
    /**
     * Appointment table view
     */
    @FXML
    private TableView<Appointment> appointmentTableView;
    /**
     * Report table view
     */
    @FXML
    private TableView<Report> typeTotalTable, monthTotalTable, locationTableView;
    /**
     * Text type table column for report data
     */
    @FXML
    private TableColumn<Report, String> colLocation, colType, colMonth;
    /**
     * Number type table column for report data
     */
    @FXML
    private TableColumn<Report, Integer> colLocationTotal, colTypeTotal, colMonthTotal;
    /**
     * Number type table column for appointment data
     */
    @FXML
    private TableColumn<Appointment,Integer> appointmentId, customerID, divisionId, customerId;
    /**
     * Text type table column for appointment data
     */
    @FXML
    private TableColumn<Appointment, String> title, customerName, description, customerAddress, location, contact, type;
    /**
     * Date type table column for appointment data
     */
    @FXML
    private TableColumn<Appointment, Date> startTime, endTime;
    /**
     * Dropdown for appointment time filtering
     */
    @FXML
    private ComboBox<String> contactBoxField, locationBox;
    /**
     * List for reportable attribute
     */
    ObservableList<Report> typeList, monthList, locationList;
    /**
     * List of contacts for appointment filtering
     */
    private List<String> contacts = new ArrayList<>();
    /**
     * Button for navigating/filtering on reports screen
     */
    @FXML
    private Button addAppointmentButton, updateAppointmentButton, deleteAppointmentButton, userButton, contactButton, customerButton, exitButton;
    /**
     * Reports Controller, saves local data store
     * @param ds Data store with local session data
     */
    public ReportsController(DataStore ds) {
        this.ds = ds;
    }
    /**
     * Initializes screen with translated labels, default filter settings, and
     * loads report data into tables
     * @param url screen url
     * @param resourceBundle text localization resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadLabels();
        // total appointments by type
        loadTypeList();
        // total appointments by month
        loadMonthList();
        // total appointments by location
        loadLocationList();
        // appointments by contact
        loadAppointmentContacts();
    }
    /**
     * Sets strings in user interface to translated text
     */
    private void loadLabels() {
        //set translated text to fields on string
        appointmentTitle.setText(rb.getString("reports"));
        appointmentId.setText(rb.getString("id"));
        title.setText(rb.getString("title"));
        description.setText(rb.getString("description"));
        type.setText(rb.getString("type"));

        startTime.setText(rb.getString("start_time"));
        endTime.setText(rb.getString("end_time"));
        customerId.setText(rb.getString("customer_id"));

        contactButton.setText(rb.getString("contacts"));
        customerButton.setText(rb.getString("customers"));
        exitButton.setText(rb.getString("exit"));
    }

    /**
     * Load report for appointment counts by type
     */
    private void loadTypeList() {
        typeList = FXCollections.observableArrayList();
        ObservableList<String> typeData = FXCollections.observableArrayList();
        ArrayList <Integer> typeCount = new ArrayList<>();
        //loop through all appointments and get type summary
        for (int row = 0; row < ds.getAllAppointments().size(); row++) {
            //add new types to type list
            if (!typeData.contains(ds.getAllAppointments().get(row).getType())) {
                typeData.add(ds.getAllAppointments().get(row).getType());
                typeCount.add(1);
            } else {
                //add to count number for existing types
                Integer typeIndex = typeData.indexOf(ds.getAllAppointments().get(row).getType());
                typeCount.set(typeIndex, typeCount.get(typeIndex) +1 );
            }
        }
        // loop through unique types and add to report
        for (int row = 0; row < typeData.size(); row++) {
            typeList.add(new Report(
                    new ReadOnlyObjectWrapper(typeCount.get(row)),
                    new ReadOnlyStringWrapper(typeData.get(row))
            ));
        }
        /**
         * Lambda processing on appointment report to reduce code and allow for higher efficiency
         * in setting report collection data to table by allowing sequential/parallel processing
         */
        // set table columns and table data
        colType.setCellValueFactory(cellData -> {
            return cellData.getValue().getAttribute();
        });
        colTypeTotal.setCellValueFactory(cellData -> {
            return cellData.getValue().getCount();
        });
        typeTotalTable.setItems(typeList);
    }

    /**
     * Load reports for appointment counts by month into table
     */
    private void loadMonthList() {
        monthList = FXCollections.observableArrayList();
        ObservableList<String> monthData = FXCollections.observableArrayList();
        ArrayList <Integer> monthCount = new ArrayList<>();
        String month = "";
        //loop through all appointments and get month summary
        for (int row = 0; row < ds.getAllAppointments().size(); row++) {
            Locale locale = Locale.getDefault();
            month = ds.getAllAppointments().get(row).getLocalStart().toLocalDateTime().getMonth().getDisplayName(TextStyle.FULL, locale);
            //add new month to month list
            if (!monthData.contains(month)) {
                monthData.add(month);
                monthCount.add(1);
            } else {
                //add to count number for existing months
                monthData.indexOf(month);
                monthCount.set(monthData.indexOf(month), monthCount.get(monthData.indexOf(month)) +1 );
            }
        }
        // loop through unique months and add to report
        for (int row = 0; row < monthData.size(); row++) {
            monthList.add(new Report(
                    new ReadOnlyObjectWrapper(monthCount.get(row)),
                    new ReadOnlyStringWrapper(monthData.get(row))
            ));
        }
        /**
         * Lambda processing on appointment report to reduce code and allow for higher efficiency
         * in setting report collection data to table by allowing sequential/parallel processing
         */
        colMonth.setCellValueFactory(cellData -> {
            return cellData.getValue().getAttribute();
        });
        colMonthTotal.setCellValueFactory(cellData -> {
            return cellData.getValue().getCount();
        });
        monthTotalTable.setItems(monthList);
    }
    /**
     * Load report for location counts of appointments
     */
    private void loadLocationList() {
        locationList = FXCollections.observableArrayList();
        ObservableList<String> locationData = FXCollections.observableArrayList();
        ArrayList <Integer> locationCount = new ArrayList<>();
        //loop through all appointments and get location summary
        for (int row = 0; row < ds.getAllAppointments().size(); row++) {
            //add new location to location list
            if (!locationData.contains(ds.getAllAppointments().get(row).getLocation())) {
                locationData.add(ds.getAllAppointments().get(row).getLocation());
                locationCount.add(1);
            } else {
                //add to count number for existing locations
                locationData.indexOf(ds.getAllAppointments().get(row).getLocation());
                locationCount.set(locationData.indexOf(ds.getAllAppointments().get(row).getLocation()), locationCount.get(locationData.indexOf(ds.getAllAppointments().get(row).getLocation())) +1 );
            }
        }
        // loop through unique locations and add to report
        for (int row = 0; row < locationData.size(); row++) {
            locationList.add(new Report(
                    new ReadOnlyObjectWrapper(locationCount.get(row)),
                    new ReadOnlyStringWrapper(locationData.get(row))
            ));
        }
        /**
         * Lambda processing on appointment report to reduce code and allow for higher efficiency
         * in setting report collection data to table by allowing sequential/parallel processing
         */
        colLocation.setCellValueFactory(cellData -> {
            return cellData.getValue().getAttribute();
        });
        colLocationTotal.setCellValueFactory(cellData -> {
            return cellData.getValue().getCount();
        });
        locationTableView.setItems(locationList);
    }

    /**
     * Load contacts into dropdown for appointment filtering
     */
    private void loadAppointmentContacts() {
        // clear contacts
        this.contactBoxField.getItems().clear();
        this.contacts.clear();
        // add "Any" option to dropdown and set as default
        contactBoxField.getItems().add(rb.getString("any"));
        contactBoxField.setValue(rb.getString("any"));
        //loop through all contacts and add to dropdown
        for (int i = 0; i < ds.getAllContacts().size(); i++ ) {
            contactBoxField.getItems().add(ds.getAllContacts().get(i).getName());
            this.contacts.add(ds.getAllContacts().get(i).getName());
        }
        //set table to all appointments by default
        appointmentTableView.setItems(ds.getAllAppointments());
    }
    /**
     * Filters appointment table to appointments for selected contact
     * @param event Contact dropdown change
     */
    @FXML
    private void handleContactBoxAction(ActionEvent event) {
        String selectedContact = contactBoxField.getSelectionModel().getSelectedItem();
        FilteredList<Appointment> filteredData = new FilteredList<>(ds.getAllAppointments());
        /**
         * Lambda filter on appointments to reduce code and allow for higher efficiency
         * in sorting through the appointment collection by allowing sequential/parallel processing
         */
        filteredData.setPredicate(row -> {
            Integer contactId = row.getContactId();
            //return appointment if selected contact is set to "Any" or same contact as appointment
            if (selectedContact == "Any" || selectedContact == ds.getAllContacts().get(contactId - 1).getName()) {
                return true;
            }
            return false;
        });
        //filter appointments by contact name
        appointmentTableView.setItems(filteredData);
    }
    /**
     * Navigates to appointment management screen
     * @param event Button click event
     * @throws IOException Fails to load appointment screen
     */
    @FXML
    private void handleAppointmentButtonAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/appointments.fxml"));
        Controller.AppointmentController controller = new Controller.AppointmentController(ds);
        loader.setController(controller);
        Scene scene = new Scene((Pane)loader.load());
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
        Scene scene = new Scene((Pane)loader.load());
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Navigates to customer management screen
     * @param event Button click event
     * @throws IOException Fails to load customer screen
     */
    @FXML
    private void handleCustomerButtonAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/customers.fxml"));
        Controller.CustomerController controller = new Controller.CustomerController(ds);
        loader.setController(controller);
        Scene scene = new Scene((Pane)loader.load());
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
