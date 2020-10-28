package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Locale;
/**
 * Saves local data for user session
 * and local appointment data from database
 */
public class DataStore {
    /**
     * Represents logged in user of local session
     */
    private String user;
    /**
     * Local user's system-detected region
     */
    private Locale locale;
    /**
     *  List of all customers
     */
    private ObservableList<Customer> allCustomers;
    /**
     * List of all contacts
     */
    private ObservableList<Contact> allContacts;
    /**
     * List of all appointments
     */
    private ObservableList<Appointment> allAppointments;

    /**
     * Creates local data store of appointment data from database
     */
    public DataStore() {
        allCustomers = FXCollections.observableArrayList();
        allContacts = FXCollections.observableArrayList();
        allAppointments = FXCollections.observableArrayList();
    }

    /**
     * Adds logged in user
     * @param authedUser User logging in
     */
    public void addUser(String authedUser) {
        if (authedUser != null) {
            user = authedUser;
        }
    }

    /**
     * Adds new contact to list of contacts
     * @param newContact New contact to add
     */
    public void addContact(Contact newContact) {
        if (newContact != null) {
            allContacts.add(newContact);
        }
    }

    /**
     * Adds new customer to list of customers
     * @param newCustomer New customer to add
     */
    public void addCustomer(Customer newCustomer) {
        if (newCustomer != null) {
            allCustomers.add(newCustomer);
        }
    }

    /**
     * Adds new appointment to list of appointments
     * @param newAppointment New appointment to add
     */
    public void addAppointment(Appointment newAppointment) {
        if (newAppointment != null) {
            allAppointments.add(newAppointment);
        }
    }

    /**
     * Updates logged in user's region
     * @param newLocale New locale of user
     */
    public void updateLocale(Locale newLocale) {
        if (newLocale != null) {
            locale = newLocale;
        }
    }

    /**
     * Gets logged in user's region
     * @return Locale of local session user
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Gets logged in user's username
     * @return Logged in user for local session
     */
    public String getUser() {
        return user;
    }

    /**
     * Gets list of all customers
     * @return List of all customers
     */
    public ObservableList<Customer> getAllCustomers() {
        return allCustomers;
    }

    /**
     * Gets list of all contacts
     * @return List of all contacts
     */
    public ObservableList<Contact> getAllContacts() {
        return allContacts;
    }

    /**
     * Gets list of all appointments
     * @return List of all appointments
     */
    public ObservableList<Appointment> getAllAppointments() {
        return allAppointments;
    }
}
