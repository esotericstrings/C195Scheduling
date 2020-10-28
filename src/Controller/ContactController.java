package Controller;

import Model.DataStore;
import Model.Contact;
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
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for contact management screen
 */
public class ContactController implements Initializable {
    /**
     * Loads resource bundle for localization/translation of text strings
     */
    private ResourceBundle rb = ResourceBundle.getBundle("language_files/rb");
    /**
     * Stores local user session data and appointment information from database
     */
    private final DataStore ds;
    /**
     * Title for contact screen
     */
    @FXML
    private Text contactTitle;
    /**
     * Contact table view
     */
    @FXML
    private TableView<Contact> contactTableView;
    /**
     * Number type table column for contact data
     */
    @FXML
    private TableColumn<Contact,Integer> contactID;
    /**
     * Text type table column for contact data
     */
    @FXML
    private TableColumn<Contact, String> contactName, contactEmail;

    /**
     * Button for navigating/filtering on contact screen
     */
    @FXML
    private Button addContactButton, updateContactButton, deleteContactButton, appointmentButton, exitButton;

    /**
     * Contact Controller, saves local data store
     * @param ds Data store with local session data
     */
    public ContactController(DataStore ds) {
        this.ds = ds;
    }

    /**
     * Initializes screen with translated labels and
     * loads contact data into table
     * @param url screen url
     * @param resourceBundle text localization resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadLabels();
        //set data in table
        contactTableView.setItems(ds.getAllContacts());
    }

    /**
     * Sets strings in user interface to translated text
     */
    private void loadLabels() {
        //set translated text to fields on string
        contactTitle.setText(rb.getString("contacts"));
        contactID.setText(rb.getString("id"));
        contactName.setText(rb.getString("name"));
        contactEmail.setText(rb.getString("email"));
        addContactButton.setText(rb.getString("add"));
        updateContactButton.setText(rb.getString("update"));
        deleteContactButton.setText(rb.getString("delete"));
        appointmentButton.setText(rb.getString("appointments"));
        exitButton.setText(rb.getString("exit"));
    }

    /**
     * Navigates to add contact screen
     * @param event Button click event
     * @throws IOException Fails to load add appointment screen
     */
    @FXML
    private void handleAddContactButtonAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/addcontact.fxml"));
        Controller.AddContactController controller = new Controller.AddContactController(ds);
        loader.setController(controller);
        Scene scene = new Scene((Pane)loader.load());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Navigates to contact modification and passes selected contact to page
     * or triggers error alert if appointment is not selected
     * @param event Button click event
     * @throws IOException Fails to load contact modification screen
     */
    @FXML
    private void handleModifyContactButtonAction(ActionEvent event) throws IOException {
        //get user selected contact from table
        Contact selectedContact = contactTableView.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/modifycontact.fxml"));
            //pass selected appointment information to modify appointment controller
            Controller.ModifyContactController controller = new Controller.ModifyContactController(ds, selectedContact);
            loader.setController(controller);
            Scene scene = new Scene((Pane) loader.load());
            stage.setScene(scene);
            stage.show();
        } else {
            //show error message if no contact is selected for modification
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("error"));
            alert.setContentText(rb.getString("select_contact"));
            alert.showAndWait();
        }
    }

    /**
     * Deletes selected contact from database and local data store and triggers
     * appropriate alert for successful/failed deletion attempt
     * @param event Button click event
     */
    @FXML
    private void handleDeleteContactButtonAction(ActionEvent event) {
        ObservableList<Contact> data = FXCollections.observableArrayList();
        if (contactTableView.getSelectionModel().getSelectedItem() != null) {
            data = contactTableView.getItems();
            // delete contact by selected ID
            var deleteSql = "DELETE FROM contacts WHERE Contact_ID = "+contactTableView.getSelectionModel().getSelectedItem().getId();
            try (var ps = DBConnect.getConn().prepareStatement(deleteSql)) {
                ps.executeUpdate();
            } catch(SQLException e) {
                // show error if customer deletion fails
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rb.getString("error"));
                alert.setHeaderText(rb.getString("contact_delete_error_title"));
                alert.setContentText(rb.getString("contact_delete_error_message"));
                alert.showAndWait();
                e.printStackTrace();
            }

            // delete selected contact from local table
            data.remove(contactTableView.getSelectionModel().getSelectedItem());
            contactTableView.setItems(data);
        } else {
            //show error message if no appointment is selected for deletion
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("error"));
            alert.setContentText(rb.getString("contact_not_found"));
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
        if (result.get() == ButtonType.OK){
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }
}
