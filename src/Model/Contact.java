package Model;
/**
 * Represents a contact of the consulting firm.
 * A contact can work with many different users and customers but
 * cannot be in more than one meeting at once.
 */
public class Contact {
    /**
     * Contact ID
     */
    private int contactId;
    /**
     * Name of contact
     */
    private String contactName;
    /**
     * Email of contact
     */
    private String email;

    /**
     * Creates a contact object to represent a contact of
     * the consulting firm
     * @param contactId Contact ID
     * @param contactName Name of contact
     * @param email Email of contact
     */
    public Contact(int contactId, String contactName, String email) {
        super();
        setId(contactId);
        setName(contactName);
        setEmail(email);
    }

    /**
     * Sets contact ID
     * @param contactId Contact ID
     */
    public void setId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * Sets contact name
     * @param contactName Name of contact
     */
    public void setName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * Sets contact email
     * @param email Email of contact
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets contact ID
     * @return Contact ID
     */
    public int getId() {
        return contactId;
    }

    /**
     * Gets contact name
     * @return Name of contact
     */
    public String getName() {
        return contactName;
    }

    /**
     * Gets contact email
     * @return Email of contact
     */
    public String getEmail() {
        return email;
    }
}
