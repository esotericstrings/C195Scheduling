package Controller;

import Model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utils.DBConnect;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;

/**
 * Controller for new customer screen
 */
public class AddCustomerController implements Initializable {
    /**
     * Loads resource bundle for localization/translation of text strings
     */
    private ResourceBundle rb = ResourceBundle.getBundle("language_files/rb");
    /**
     * Stores local user session data and appointment information from database
     */
    private final DataStore ds;
    /**
     * New customer form label
     */
    @FXML
    private Label addCustomerTitle, customerIdLabel, customerNameLabel, addressLabel, postalCodeLabel, phoneLabel, countryLabel, divisionBoxLabel;
    /**
     * Text input field to add new customer
     */
    @FXML
    private TextField customerIdField, customerNameField, addressField, postalCodeField, phoneField, divisionIdField;
    /**
     * Dropdown of available locations
     */
    @FXML
    private ComboBox<String> divisionBoxField, countryBoxField;
    /**
     * Button for add customer form
     */
    @FXML
    private Button createNewCustomerAction, cancelButton;
    /**
     * List of available countries
     */
    private List<String> countries = new ArrayList<>();
    /**
     * List of available divisions
     */
    private List<String> divisions = new ArrayList<>();

    /**
     * Controller for new customer screen
     * @param ds Data store with local session data
     */
    public AddCustomerController(DataStore ds) {
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
    }
    /**
     * Sets strings in user interface to translated text
     */
    private void loadLabels() {
        customerIdLabel.setText(rb.getString("id"));
        addCustomerTitle.setText(rb.getString("add_customer"));
        customerNameLabel.setText(rb.getString("name"));
        addressLabel.setText(rb.getString("address"));
        postalCodeLabel.setText(rb.getString("postal_code"));
        phoneLabel.setText(rb.getString("phone_number"));
        countryLabel.setText(rb.getString("country"));
        divisionBoxLabel.setText(rb.getString("division"));
        customerIdField.setPromptText(rb.getString("disabled_auto_gen"));
        customerNameField.setPromptText(rb.getString("customer_name"));
        addressField.setPromptText(rb.getString("address"));
        postalCodeField.setPromptText(rb.getString("postal_code"));
        phoneField.setPromptText(rb.getString("phone_number"));
        createNewCustomerAction.setText(rb.getString("save"));
        cancelButton.setText(rb.getString("cancel"));
    }
    /**
     * Load dropdowns with available country/division options
     */
    @FXML
    private void loadComboBoxes() {
        this.countryBoxField.getItems().clear();
        this.countries.clear();
        String rq1 = "SELECT Country FROM countries";
        String rq2 = "SELECT Division FROM first_level_divisions d";
        try {
            Statement st1 = DBConnect.getConn().createStatement();
            Statement st2 = DBConnect.getConn().createStatement();
            ResultSet rs1 = st1.executeQuery(rq1);
            ResultSet rs2 = st2.executeQuery(rq2);
            while (rs1.next()) {
                String country = rs1.getString("Country");
                this.countryBoxField.getItems().add(country);
                this.countries.add(country);
            }
            while (rs2.next()) {
                String division = rs2.getString("Division");
                this.divisionBoxField.getItems().add(division);
                this.divisions.add(division);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateDropdown(ActionEvent event) {
        String selectedCountry = countryBoxField.getValue();
        String sql = "SELECT d.Division_ID, d.Division, d.COUNTRY_ID, c.Country_ID, c.Country FROM first_level_divisions d JOIN countries c ON (d.COUNTRY_ID = c.Country_ID) WHERE c.Country = '"+selectedCountry+"';";
        try {
            PreparedStatement myInsert = DBConnect.getConn().prepareStatement(sql);
            Statement st1 = DBConnect.getConn().createStatement();
            ResultSet rs1 = st1.executeQuery(sql);
            this.divisionBoxField.getItems().removeAll(this.divisions);
            this.divisions.clear();
            while (rs1.next()) {
                String division = rs1.getString("Division");
                this.divisionBoxField.getItems().add(division);
                this.divisions.add(division);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Adds new customer
     * @param event Button click event
     * @throws IOException Fails to load customer management screen
     */
    @FXML
    private void createNewCustomerAction(ActionEvent event) throws IOException {
        boolean valid = true;
        int newId = ds.getAllCustomers().get(ds.getAllCustomers().size() - 1).getCustomerId()+1;
        String customerNameText = customerNameField.getText();
        String addressText = addressField.getText();
        String postalCodeText = postalCodeField.getText();
        String phoneText = phoneField.getText();
        java.sql.Date current_date = new java.sql.Date(System.currentTimeMillis());
        String created_by = ds.getUser();
        Timestamp current_time = new Timestamp( new java.sql.Date(System.currentTimeMillis()).getTime());
        String last_updated_by = ds.getUser();
        String divisionText = divisionBoxField.getSelectionModel().getSelectedItem();
        String countryText = countryBoxField.getSelectionModel().getSelectedItem();
        Customer c1 = new Customer(newId, customerNameText, addressText, postalCodeText, phoneText, current_date, created_by, current_time, last_updated_by, divisionText, countryText);
        try {
            String rq1 = "SELECT * FROM first_level_divisions WHERE Division = '"+divisionText+"';";
            Statement st1 = DBConnect.getConn().createStatement();
            ResultSet rs1 = st1.executeQuery(rq1);
            int divisionId = 0;
            while (rs1.next()) {
                divisionId = rs1.getInt("Division_ID");
            }
            String rq2 = "INSERT INTO customers VALUES(?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement myInsert = DBConnect.getConn().prepareStatement(rq2);
            myInsert.setInt(1,newId);
            myInsert.setString(2,customerNameText);
            myInsert.setString(3,addressText);
            myInsert.setString(4,postalCodeText);
            myInsert.setString(5,phoneText);
            myInsert.setDate(6, current_date);
            myInsert.setString(7,created_by);
            myInsert.setTimestamp(8, current_time);
            myInsert.setString(9, last_updated_by);
            myInsert.setInt(10, divisionId);
            try (var ps = myInsert) {
                ps.executeUpdate();
            }
            ds.addCustomer(c1);
        } catch(Exception e) {
            e.printStackTrace();
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(rb.getString("error"));
        alert.setHeaderText(rb.getString("error"));

        if (valid) {
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/customers.fxml"));
            Controller.CustomerController controller = new Controller.CustomerController(ds);
            loader.setController(controller);
            Scene scene = new Scene((Pane)loader.load());
            stage.setScene(scene);
            stage.show();
        } else {
            alert.showAndWait();
        }
    }
    /**
     * Exits add customer screen with
     * user confirmation that changes will not save
     * @param event Button click event
     * @throws IOException Fails to load customer screen
     */
    @FXML
    private void handleCancelButtonAction(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(rb.getString("cancel_confirmation"));
        alert.setContentText(rb.getString("cancel_customer_message"));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/customers.fxml"));
            Controller.CustomerController controller = new Controller.CustomerController(ds);
            loader.setController(controller);
            Scene scene = new Scene((Pane)loader.load());
            stage.setScene(scene);
            stage.show();
        }
    }
}
