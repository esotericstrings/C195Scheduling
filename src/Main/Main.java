package Main;
import Model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utils.DBConnect;

import java.sql.*;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Initializes application, loading text localization files, startup data, and
 * initial screen
 */
public class Main extends Application {
    /**
     * Load resource bundle for localization
     * Fetches application data from DB and saves local copy
     * Initializes application on login screen
     * @param primaryStage Initial stage of application
     * @throws Exception Fails to load appropriate text localization resource bundle
     * for user's system setting
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        DataStore ds = new DataStore();
        loadData(ds);
        // Load resource bundle for localization
        try {
            ResourceBundle rb = ResourceBundle.getBundle("language_files/rb");
            Locale locale = Locale.getDefault();
            ds.updateLocale(locale);
        } catch(Exception e) {
            e.printStackTrace();
        }
        // Navigate to login screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/login.fxml"));
        Controller.LoginController controller = new Controller.LoginController(ds);
        loader.setController(controller);
        Stage stage = new Stage();
        Scene scene = new Scene((Pane)loader.load());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Fetches appointment, customer, contact data from WGU database
     * @param ds Local data store where session data is saved
     * @throws SQLException Fails to fetch customer/appointment/contact information from database
     */
    void loadData(DataStore ds) throws SQLException {
            try {
                // load customer information from database
                String sql = "SELECT c.Customer_ID, c.Customer_Name, c.Address, c.Postal_Code, c.Phone, c.Create_Date, c.Created_By, c.Last_Update, c.Last_Updated_By, c.Division_ID, d.Division, n.Country FROM customers c JOIN first_level_divisions d ON (c.Division_ID = d.Division_ID) JOIN countries n ON (d.COUNTRY_ID = n.Country_ID);";
                try (var ps = DBConnect.getConn().prepareStatement(sql);
                     ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("c.Customer_ID");
                        String name = rs.getString("c.Customer_Name");
                        String address = rs.getString("c.Address");
                        String postal = rs.getString("c.Postal_Code");
                        String phone = rs.getString("c.Phone");
                        Date create_date = rs.getDate("c.Create_Date");
                        String created_by = rs.getString("c.Created_By");
                        Timestamp last_update = rs.getTimestamp("c.Last_Update");
                        String last_updated_by = rs.getString("c.Last_Updated_By");
                        String division = rs.getString("d.Division");
                        String country = rs.getString("n.Country");
                        Customer c1 = new Customer(id, name, address, postal, phone, create_date, created_by, last_update, last_updated_by, division, country);
                        ds.addCustomer(c1);
                    }
                }
                // load appointment information from database
                String sql2 = "SELECT * FROM appointments a JOIN contacts c ON (a.Contact_ID = c.Contact_ID) JOIN customers m ON (a.Customer_ID = m.Customer_ID) JOIN users u ON (a.User_ID = u.User_ID);";
                try (var ps = DBConnect.getConn().prepareStatement(sql2);
                     ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("a.Appointment_ID");
                        String title = rs.getString("a.Title");
                        String description = rs.getString("a.Description");
                        String location = rs.getString("a.Location");
                        String type = rs.getString("a.Type");
                        int contactId = rs.getInt("a.Contact_ID");
                        Timestamp start = rs.getTimestamp("a.Start");
                        Timestamp end = rs.getTimestamp("a.End");
                        Timestamp create_date = rs.getTimestamp("a.Create_Date");
                        String created_by = rs.getString("a.Created_By");
                        Timestamp last_update = rs.getTimestamp("a.Last_Update");
                        String last_updated_by = rs.getString("a.Last_Updated_By");
                        int customerId = rs.getInt("a.Customer_ID");
                        int userId = rs.getInt("a.User_ID");
                        Appointment c1 = new Appointment(id, title, description, location, type, contactId, start, end, create_date, created_by, last_update, last_updated_by, customerId, userId);
                        ds.addAppointment(c1);
                    }
                }
                // load contact information from database
                String sql3 = "SELECT * FROM contacts c;";
                try (var ps = DBConnect.getConn().prepareStatement(sql3);
                     ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("c.Contact_ID");
                        String name = rs.getString("c.Contact_Name");
                        String email = rs.getString("c.Email");
                        Contact c1 = new Contact(id, name, email);
                        ds.addContact(c1);
                    }
                }

            } catch(Exception e) {
                e.printStackTrace();
            }
    }

    /**
     * Launches application
     */
    public static void main() {
        launch();
    }
}
