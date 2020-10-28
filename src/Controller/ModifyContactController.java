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
 * Controller for contact updating screen
 */
public class ModifyContactController implements Initializable {
    /**
     * Loads resource bundle for localization/translation of text strings
     */
    private ResourceBundle rb = ResourceBundle.getBundle("language_files/rb");
    /**
     * Stores local user session data and appointment information from database
     */
    private final DataStore ds;
    /**
     * Contact selected for modification
     */
    private final Contact contact;
    /**
     * Contact modification form label
     */
    @FXML
    private Label contactTitle, contactIdLabel, contactNameLabel, contactEmailLabel;
    /**
     * Text input field to modify contact information
     */
    @FXML
    private TextField contactIdField, contactNameField, contactEmailField;
    /**
     * Button for contact modification form
     */
    @FXML
    private Button saveButton, cancelButton;

    /**
     * Controller for contact modification screen
     * @param ds Data store with local session data
     * @param contact Selected contact to be modified
     */
    public ModifyContactController(DataStore ds, Contact contact) {
        this.ds = ds;
        this.contact = contact;
    }
    /**
     * Initializes screen with translated labels, default dropdown settings, and
     * loads contact data into input fields for modification
     * @param url screen url
     * @param resourceBundle text localization resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadLabels();
        setData();
    }
    /**
     * Sets strings in user interface to translated text
     */
    private void loadLabels() {
        contactTitle.setText(rb.getString("modify_contact"));
        contactIdLabel.setText(rb.getString("id"));
        contactIdField.setText(rb.getString("disabled_auto_gen"));
        contactNameField.setPromptText(rb.getString("name"));
        contactNameLabel.setText(rb.getString("name"));
        contactEmailLabel.setText(rb.getString("email"));
        contactEmailField.setPromptText(rb.getString("email"));
        saveButton.setText(rb.getString("save"));
        cancelButton.setText(rb.getString("cancel"));
    }
    /**
     * Sets input fields with existing contact data for modification
     */
    private void setData() {
        contactIdField.setText(Integer.toString(contact.getId()));
        contactNameField.setText(contact.getName());
        contactEmailField.setText(contact.getEmail());
    }
    /**
     * Updates modification to contact
     * @param event Button click event
     * @throws IOException Fails to load contact management screen
     * @throws SQLException Fails to update contact in database
     */
    @FXML
    private void handleSaveContactButtonAction(ActionEvent event) throws IOException, SQLException {
        boolean valid = false;
        int newContactId = Integer.parseInt(contactIdField.getText());
        String newContactName = contactNameField.getText();
        String newEmail = contactEmailField.getText();
        // attempt to update contacts in db with user inputted info
        String updateSql = "UPDATE contacts SET Contact_Name = ?, Email = ? WHERE Contact_ID = ?";
        PreparedStatement myInsert = DBConnect.getConn().prepareStatement(updateSql);
        myInsert.setString(1,newContactName);
        myInsert.setString(2,newEmail);
        myInsert.setInt(3,newContactId);
        try (var ps = myInsert) {
            myInsert.executeUpdate();
            //update local contact info if info is valid db updates successfully
            contact.setId(newContactId);
            contact.setName(newContactName);
            contact.setEmail(newEmail);
            valid = true;
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
            //throw error if contact info is not valid
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("error"));
            alert.showAndWait();
        }
    }
    /**
     * Exits contact modification screen with
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
