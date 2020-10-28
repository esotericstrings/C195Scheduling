package Controller;

import Model.DataStore;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import utils.DBConnect;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
/**
 * Controller for customer management screen
 */
public class CustomerController implements Initializable {
    /**
     * Loads resource bundle for localization/translation of text strings
     */
    private ResourceBundle rb = ResourceBundle.getBundle("language_files/rb");
    /**
     * Stores local user session data and appointment information from database
     */
    private final DataStore ds;
    /**
     * Title for customer screen
     */
    @FXML
    private Text customerTitle;
    /**
     * Customer table view
     */
    @FXML
    private TableView<Customer> customerTableView;
    /**
     * Number type table column for customer data
     */
    @FXML
    private TableColumn<Customer,Integer> customerID, divisionId;
    /**
     * Text type table column for customer data
     */
    @FXML
    private TableColumn<Customer, String> customerName, customerAddress, postalCode, phoneNumber, country, createdBy, lastUpdatedBy;
    /**
     * Date type table column for customer data
     */
    @FXML
    private TableColumn<Customer, Date> createDate;
    /**
     * Timestamp type table column for customer data
     */
    @FXML
    private TableColumn<Customer, Timestamp> lastUpdate;

    /**
     * Button for navigating/filtering on customer screen
     */
    @FXML
    private Button addCustomerButton, updateCustomerButton, deleteCustomerButton, appointmentButton, exitButton;
    /**
     * Customer Controller, saves local data store
     * @param ds Data store with local session data
     */
    public CustomerController(DataStore ds) {
        this.ds = ds;
    }
    /**
     * Initializes screen with translated labels and
     * loads customer data into table
     * @param url screen url
     * @param resourceBundle text localization resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadLabels();
        //load customer data to table
        customerTableView.setItems(ds.getAllCustomers());
    }
    /**
     * Sets strings in user interface to translated text
     */
    private void loadLabels() {
        //set translated text to fields on string
        customerTitle.setText(rb.getString("customer_records"));
        customerID.setText(rb.getString("id"));
        customerName.setText(rb.getString("name"));
        customerAddress.setText(rb.getString("address"));
        postalCode.setText(rb.getString("postal_code"));
        phoneNumber.setText(rb.getString("phone_number"));
        divisionId.setText(rb.getString("division"));
        country.setText(rb.getString("country"));
        addCustomerButton.setText(rb.getString("add"));
        updateCustomerButton.setText(rb.getString("update"));
        deleteCustomerButton.setText(rb.getString("delete"));
        appointmentButton.setText(rb.getString("appointments"));
        exitButton.setText(rb.getString("exit"));
    }
    /**
     * Navigates to add customer screen
     * @param event Button click event
     * @throws IOException Fails to load add appointment screen
     */
    @FXML
    private void handleAddCustomerButtonAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/addcustomer.fxml"));
        Controller.AddCustomerController controller = new Controller.AddCustomerController(ds);
        loader.setController(controller);
        Scene scene = new Scene((Pane)loader.load());
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Navigates to customer modification and passes selected customer to page
     * or triggers error alert if appointment is not selected
     * @param event Button click event
     * @throws IOException Fails to load customer modification screen
     */
    @FXML
    private void handleModifyCustomerButtonAction(ActionEvent event) throws IOException {
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/modifycustomer.fxml"));
            //pass selected customer information to modify customer controller
            Controller.ModifyCustomerController controller = new Controller.ModifyCustomerController(ds, selectedCustomer);
            loader.setController(controller);
            Scene scene = new Scene((Pane) loader.load());
            stage.setScene(scene);
            stage.show();
        } else {
            //show error if customer is not selected for modification
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("error"));
            alert.setContentText(rb.getString("select_customer"));
            alert.showAndWait();
        }
    }
    /**
     * Deletes selected customer from database and local data store and triggers
     * appropriate alert for successful/failed deletion attempt
     * @param event Button click event
     * @throws SQLException Fails to delete selected customer from database
     */
    @FXML
    private void handleDeleteCustomerButtonAction(ActionEvent event) throws SQLException {
        ObservableList<Customer> data = FXCollections.observableArrayList();
        if (customerTableView.getSelectionModel().getSelectedItem() != null) {
            data = customerTableView.getItems();
            try {
                var deleteSql = "DELETE FROM customers WHERE Customer_ID = "+customerTableView.getSelectionModel().getSelectedItem().getCustomerId();
                try (var ps = DBConnect.getConn().prepareStatement(deleteSql)) {
                    ps.executeUpdate();
                }
                data.remove(customerTableView.getSelectionModel().getSelectedItem());
                customerTableView.setItems(data);
                // show successful deletion message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(rb.getString("customer_delete_title"));
                alert.setHeaderText(rb.getString("customer_delete_title"));
                alert.setContentText(rb.getString("customer_delete_message"));
                alert.showAndWait();
            } catch(SQLException e) {
                // show error if customer deletion fails
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rb.getString("error"));
                alert.setHeaderText(rb.getString("customer_delete_error_title"));
                alert.setContentText(rb.getString("customer_delete_error_message"));
                alert.showAndWait();
                e.printStackTrace();
            }
        } else {
            //show error if customer is not selected for deletion
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("error"));
            alert.setContentText(rb.getString("customer_not_found"));
            alert.showAndWait();
        }
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
