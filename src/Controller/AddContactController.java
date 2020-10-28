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
 * Controller for new contact screen
 */
public class AddContactController implements Initializable {
    /**
     * Loads resource bundle for localization/translation of text strings
     */
    private ResourceBundle rb = ResourceBundle.getBundle("language_files/rb");
    /**
     * Stores local user session data and appointment information from database
     */
    private final DataStore ds;
    /**
     * Text input field to add new contact
     */
    @FXML
    private TextField contactIdField, contactNameField, contactEmailField;
    /**
     * Button for add contact form
     */
    @FXML
    private Button createNewContactAction, cancelButton;
    /**
     * New contact form label
     */
    @FXML
    private Label contactTitle, contactIdLabel, contactNameLabel, contactEmailLabel;

    /**
     * Controller for new contact screen
     * @param ds Data store with local session data
     */
    public AddContactController(DataStore ds) {
        this.ds = ds;
    }
    /**
     * Initializes screen with translated labels
     * @param url screen url
     * @param resourceBundle text localization resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadLabels();
    }
    /**
     * Sets strings in user interface to translated text
     */
    private void loadLabels() {
        contactTitle.setText(rb.getString("add_contact"));
        contactIdLabel.setText(rb.getString("id"));
        contactIdField.setText(rb.getString("disabled_auto_gen"));
        contactNameField.setPromptText(rb.getString("name"));
        contactNameLabel.setText(rb.getString("name"));
        contactEmailLabel.setText(rb.getString("email"));
        contactEmailField.setPromptText(rb.getString("email"));
        createNewContactAction.setText(rb.getString("add"));
        cancelButton.setText(rb.getString("cancel"));
    }

    /**
     * Adds new contact
     * @param event Button click event
     * @throws IOException Fails to load contact management screen
     * @throws SQLException Fails to add contact to database
     */
    @FXML
    private void createNewContactButtonAction(ActionEvent event) throws IOException, SQLException {
        boolean valid = false;
        int contactId = ds.getAllContacts().get(ds.getAllContacts().size() - 1).getId()+1;
        String contactNameFieldText = contactNameField.getText();
        String emailFieldText = contactEmailField.getText();
        // prepare contact insertion statement with user inputted variables
        String rq2 = "INSERT INTO contacts VALUES(?,?,?)";
        PreparedStatement myInsert = DBConnect.getConn().prepareStatement(rq2);
        myInsert.setInt(1,contactId);
        myInsert.setString(2,contactNameFieldText);
        myInsert.setString(3,emailFieldText);
        try (var ps = myInsert) {
            ps.executeUpdate();
            // add contact to local data store if input is valid
            valid = true;
            Contact c1 = new Contact(contactId, contactNameFieldText, emailFieldText);
            ds.addContact(c1);
        }

        if (valid) {
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/contacts.fxml"));
            Controller.ContactController controller = new Controller.ContactController(ds);
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
     * Exits add contact screen with
     * user confirmation that changes will not save
     * @param event Button click event
     * @throws IOException Fails to load contact screen
     */
    @FXML
    private void handleCancelButtonAction(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(rb.getString("cancel_confirmation"));
        alert.setContentText(rb.getString("cancel_contact_message"));
        Optional<ButtonType> result = alert.showAndWait();
        //return to contacts without saving if user confirms exit
        if (result.get() == ButtonType.OK){
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/contacts.fxml"));
            Controller.ContactController controller = new Controller.ContactController(ds);
            loader.setController(controller);
            Scene scene = new Scene((Pane)loader.load());
            stage.setScene(scene);
            stage.show();
        }
    }
}
