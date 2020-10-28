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
import java.util.Date;

/**
 * Controller for customer updating screen
 */
public class ModifyCustomerController implements Initializable {
    /**
     * Loads resource bundle for localization/translation of text strings
     */
    private ResourceBundle rb = ResourceBundle.getBundle("language_files/rb");
    /**
     * Stores local user session data and appointment information from database
     */
    private final DataStore ds;
    /**
     * Customer selected for modification
     */
    private final Customer customer;
    /**
     * Customer modification form label
     */
    @FXML
    private Label modifyCustomerTitle, addCustomerTitle, customerIdLabel, customerNameLabel, addressLabel, postalCodeLabel, phoneLabel, countryLabel, divisionBoxLabel;
    /**
     * Text input field to modify customer information
     */
    @FXML
    private TextField customerIdField, customerNameField, addressField, postalCodeField, phoneField, countryField;
    /**
     * Dropdown input field to modify customer location information
     */
    @FXML
    private ComboBox<String> divisionBoxField, countryBoxField;
    /**
     * Button for customer modification form
     */
    @FXML
    private Button saveButton, cancelButton;
    /**
     * List of available countries
     */
    private List<String> countries = new ArrayList<>();
    /**
     * List of available divisions
     */
    private List<String> divisions = new ArrayList<>();

    /**
     * Controller for customer modification screen
     * @param ds Data store with local session data
     * @param customer Selected customer to be modified
     */
    public ModifyCustomerController(DataStore ds, Customer customer) {
        this.ds = ds;
        this.customer = customer;
    }

    /**
     * Initializes screen with translated labels, default dropdown settings, and
     * loads customer data into input fields for modification
     * @param url screen url
     * @param resourceBundle text localization resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadLabels();
        loadComboBoxes();
        setData();
    }

    /**
     * Sets strings in user interface to translated text
     */
    private void loadLabels() {
        //set translated text to fields on string
        customerIdLabel.setText(rb.getString("id"));
        modifyCustomerTitle.setText(rb.getString("modify_customer"));
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
        saveButton.setText(rb.getString("save"));
        cancelButton.setText(rb.getString("cancel"));
    }

    /**
     * Sets input fields with existing customer data for modification
     */
    private void setData() {
        //pre load data to load from customer
        customerIdField.setText(Integer.toString(customer.getCustomerId()));
        customerNameField.setText(customer.getCustomerName());
        addressField.setText(customer.getAddress());
        postalCodeField.setText(customer.getPostalCode());
        phoneField.setText(customer.getPhone());
        divisionBoxField.setValue(customer.getDivision());
        countryBoxField.setValue(customer.getCountry());
    }

    /**
     * Loads country and division dropdowns with all values
     */
    private void loadComboBoxes() {
        this.countryBoxField.getItems().clear();
        this.countries.clear();
        String rq1 = "SELECT Country_ID, Country FROM countries";
        String rq2 = "SELECT Division_ID, Division FROM first_level_divisions d";
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

    /**
     * Updates division dropdown based on selected country
     * @param event Change selected country in dropdown
     */
    @FXML
    private void updateDropdown(ActionEvent event) {
        String selectedCountry = countryBoxField.getValue();
        this.divisionBoxField.getItems().removeAll(this.divisions);
        this.divisions.clear();
        String sql = "SELECT d.Division_ID, d.Division, d.COUNTRY_ID, c.Country_ID, c.Country FROM first_level_divisions d JOIN countries c ON (d.COUNTRY_ID = c.Country_ID) WHERE c.Country = '"+selectedCountry+"';";
        try {
            Statement st1 = DBConnect.getConn().prepareStatement(sql);
            ResultSet rs1 = st1.executeQuery(sql);
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
     * Updates modification to customer
     * @param event Button click event
     * @throws IOException Fails to load customer management screen
     * @throws SQLException Fails to update customer in database
     */
    @FXML
    private void handleSaveCustomerButtonAction(ActionEvent event) throws IOException, SQLException {
        boolean valid = false;
        int newCustomerId = Integer.parseInt(customerIdField.getText());
        String newCustomerName = customerNameField.getText();
        String newAddress = addressField.getText();
        String newPostalCode = postalCodeField.getText();
        String newPhone = phoneField.getText();
        Timestamp current_time = new Timestamp( new Date().getTime());
        String last_updated_by = ds.getUser();
        String divisionText = divisionBoxField.getSelectionModel().getSelectedItem();
        String countryText = countryBoxField.getSelectionModel().getSelectedItem();
        try {
            System.out.println("Connection is successful !!!!! Updating customers...");
            String rq1 = "SELECT * FROM first_level_divisions WHERE Division = '"+divisionText+"';";
            Statement st1 = DBConnect.getConn().createStatement();
            ResultSet rs1 = st1.executeQuery(rq1);
            int divisionId = 0;
            while (rs1.next()) {
                divisionId = rs1.getInt("Division_ID");
            }
            String updateSql = "UPDATE customers SET Customer_Name = ?, ADDRESS = ?, Phone = ?, Postal_Code = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
            PreparedStatement myInsert = DBConnect.getConn().prepareStatement(updateSql);
            myInsert.setString(1,newCustomerName);
            myInsert.setString(2,newAddress);
            myInsert.setString(3,newPhone);
            myInsert.setString(4,newPostalCode);
            myInsert.setTimestamp(5, current_time);
            myInsert.setString(6, last_updated_by);
            myInsert.setInt(7, divisionId);
            myInsert.setInt(8, newCustomerId);
            customer.setId(newCustomerId);
            customer.setName(newCustomerName);
            customer.setAddress(newAddress);
            customer.setPostalCode(newPostalCode);
            customer.setPhone(newPhone);
            customer.setLastUpdate(current_time);
            customer.setLastUpdatedBy(last_updated_by);
            customer.setDivision(divisionText);
            customer.setCountry(countryText);
            try (var ps = myInsert) {
                ps.executeUpdate();
            }
            valid = true;
        } catch(Exception e) {
            e.printStackTrace();
        }
        if (valid) {
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/customers.fxml"));
            Controller.CustomerController controller = new Controller.CustomerController(ds);
            loader.setController(controller);
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Exits customer modification screen with
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
        //return to customers without saving if user confirms exit
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
