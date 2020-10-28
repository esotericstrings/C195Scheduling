package Model;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Represents a customer of the consulting firm.
 * A customer can work with many different users but
 * cannot be in more than one meeting at once.
 */
public class Customer {
    /**
     * Customer ID
     */
    private int customerId;
    /**
     * Customer name
     */
    private String customerName;
    /**
     * Customer address
     */
    private String address;
    /**
     * Customer postal code
     */
    private String postalCode;
    /**
     * Customer phone number
     */
    private String phone;
    /**
     * Date/time that customer was created on
     */
    private Date createDate;
    /**
     * User that customer was created by
     */
    private String createdBy;
    /**
     * Date/time that customer was last updated by
     */
    private Timestamp lastUpdate;
    /**
     * User that last updated customer information
     */
    private String lastUpdatedBy;
    /**
     * Customer division
     */
    private String division;
    /**
     *  Customer country
     */
    private String country;

    /**
     * Create customer object representing a customer of the consulting agency
     * @param customerId Customer ID
     * @param customerName Customer name
     * @param address Customer address
     * @param postalCode Customer postal code
     * @param phone Customer phone number
     * @param createDate Date/time that customer was created on
     * @param createdBy User that customer was created by
     * @param lastUpdate Date/time that customer was last updated by
     * @param lastUpdatedBy User that last updated customer information
     * @param division Customer division
     * @param country Customer country
     */
    public Customer(int customerId, String customerName, String address, String postalCode, String phone, Date createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy, String division, String country) {
        super();
        setId(customerId);
        setName(customerName);
        setAddress(address);
        setPostalCode(postalCode);
        setPhone(phone);
        setCreateDate(createDate);
        setCreatedBy(createdBy);
        setLastUpdate(lastUpdate);
        setLastUpdatedBy(lastUpdatedBy);
        setDivision(division);
        setCountry(country);
    }

    /**
     * Sets customer ID number
     * @param customerId Customer ID
     */
    public void setId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Sets customer name
     * @param customerName Customer name
     */
    public void setName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Sets customer address
     * @param address Customer address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Sets customer postal code
     * @param postalCode Customer postal code
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Sets customer phone number
     * @param phone Customer phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Sets date/time that customer was created on
     * @param createDate Date/time that customer was created on
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * Sets user that customer was created by
     * @param createdBy User that customer was created by
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Sets date/time that customer was last updated by
     * @param lastUpdate Date/time that customer was last updated by
     */
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * Sets username of user that last updated customer information
     * @param lastUpdatedBy User that last updated customer information
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Sets customer division
     * @param division Customer division
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * Sets customer country
     * @param country Customer country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets customer ID number
     * @return Customer ID
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Gets customer name
     * @return Customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Gets customer address
     * @return Customer address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets customer postal code
     * @return Customer postal code
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Gets customer phone number
     * @return Customer phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Gets date/time that customer was created on
     * @return Date/time that customer was created on
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * Gets user that customer was created by
     * @return User that customer was created by
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Gets date/time that customer was last updated by
     * @return Date/time that customer was last updated by
     */
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Gets user that customer was last updated by
     * @return User that customer was last updated by
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * Gets division of customer
     * @return Division that customer is in
     */
    public String getDivision() {
        return division;
    }

    /**
     * Gets country of customer
     * @return Country that customer is in
     */
    public String getCountry() {
        return country;
    }
}
