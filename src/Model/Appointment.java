package Model;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

/**
 * Represents an appointment within a consulting
 * A customer can work with many different users but
 * cannot be in more than one meeting at once.
 */
public class Appointment {
    /**
     * Appointment ID
     */
    private int appointmentId;
    /**
     * Appointment Title
     */
    private String title;
    /**
     * Appointment description
     */
    private String description;
    /**
     * Appointment location
     */
    private String location;
    /**
     * ID number of contact in appointment
     */
    private int contactId;
    /**
     * Type of appointment
     */
    private String type;
    /**
     * Appointment starting date/time in UTC timezone
     */
    private Timestamp start;
    /**
     * Appointment starting date/time in local timezone
     */
    private Timestamp localStart;
    /**
     * Appointment starting date/time in local timezone and 12-hour format
     */
    private String formattedLocalStart;
    /**
     * Appointment ending date/time in UTC timezone
     */
    private Timestamp end;
    /**
     * Appointment ending date/time in local timezone
     */
    private Timestamp localEnd;
    /**
     * Appointment ending date/time in local timezone and 12-hour format
     */
    private String formattedLocalEnd;
    /**
     * Date/time that appointment was created on
     */
    private Timestamp createDate;
    /**
     * User that appointment was created by
     */
    private String createdBy;
    /**
     * Date/time that appointment was last updated by
     */
    private Timestamp lastUpdate;
    /**
     * User that appointment was last updated by
     */
    private String lastUpdatedBy;
    /**
     * Customer ID
     */
    private int customerId;
    /**
     * User ID
     */
    private int userId;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
    /**
     * Creates appointment object representing an appointment at the global
     * consulting agency between agency users and contacts/customers of that
     * agency at a variety of office locations.
     * @param appointmentId Appointment ID
     * @param title Appointment title
     * @param description Appointment description
     * @param location Appointment office location
     * @param type Type of appointment
     * @param contactId Contact ID
     * @param start Appointment start time
     * @param end Appointment end time
     * @param createDate Date/time that appointment was created on
     * @param createdBy User that appointment was created by
     * @param lastUpdate Date/time that appointment was last updated by
     * @param lastUpdatedBy User that appointment was last updated by
     * @param customerId Customer ID
     * @param userId User ID
     */
    public Appointment(int appointmentId, String title, String description, String location, String type, int contactId, Timestamp start, Timestamp end, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy, int customerId, int userId) {
        super();
        setId(appointmentId);
        setName(title);
        setDescription(description);
        setLocation(location);
        setType(type);
        setContactId(contactId);
        setStart(start);
        setEnd(end);
        setLocalStart(start);
        setFormattedLocalStart(start);
        setLocalEnd(end);
        setFormattedLocalEnd(end);
        setCreateDate(createDate);
        setCreatedBy(createdBy);
        setLastUpdate(lastUpdate);
        setLastUpdatedBy(lastUpdatedBy);
        setCustomerId(customerId);
        setUserId(userId);
    }

    /**
     * Sets appointment ID
     * @param appointmentId Appointment ID
     */
    public void setId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * Sets appointment title
     * @param title Appointment title
     */
    public void setName(String title) {
        this.title = title;
    }

    /**
     * Sets appointment description
     * @param description Appointment description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets appointment location
     * @param location Appointment office location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Sets type of appointment
     * @param type Type of appointment
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets the appointment start time in UTC format
     * @param start Start time/date of appointment in UTC format
     */
    public void setStart(Timestamp start) {
        this.start = start;
    }

    /**
     * Sets the appointment start time in local timezone
     * @param localStart Start time/date of appointment in local timezone
     */
    public void setLocalStart(Timestamp localStart) {
        this.localStart = localStart;
    }
    /**
     * Sets the formatted local meeting start time in 12 hour, AM/PM format
     * @param localStart Appointment starting time in local timezone and Timestamp format
     */
    public void setFormattedLocalStart(Timestamp localStart) {
        this.formattedLocalStart = localStart.toLocalDateTime().format(formatter).toString();
    }

    /**
     * Sets the appointment ending time in UTC timezone
     * @param end Appointment ending time in UTC timezone and Timestamp format
     */
    public void setEnd(Timestamp end) {
        this.end = end;
    }

    /**
     * Sets the formatted local meeting end time in local timezone and Timestamp format
     * @param localEnd Appointment ending time in local timezone and Timestamp format
     */
    public void setLocalEnd(Timestamp localEnd) {
        this.localEnd = localEnd;
    }

    /**
     * Sets the formatted local meeting end time in 12 hour, AM/PM format
     * @param localEnd Appointment ending time in local timezone and Timestamp format
     */
    public void setFormattedLocalEnd(Timestamp localEnd) {
        this.formattedLocalEnd = localEnd.toLocalDateTime().format(formatter).toString();
    }

    /**
     * Get date/time that customer was created on
     * @param createDate Date/time that customer was created on
     */
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    /**
     * Sets user that appointment was created by
     * @param createdBy User that appointment was created by
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Sets date/time that appointment was last updated by
     * @param lastUpdate Date/time that appointment was last updated by
     */
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * Sets user that appointment was last updated by
     * @param lastUpdatedBy User that appointment was last updated by
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     *  Gets ID number of customer in appointment
     * @param customerId Customer ID
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Sets ID number of user in appointment
     * @param userId User ID
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Sets ID number of contact in appointment
     * @param contactId Contact ID
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * Gets appointment ID
     * @return Appointment ID
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * Gets appointment title
     * @return Appointment title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets appointment description
     * @return Appointment description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets location of office/appointment
     * @return Appointment location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets type of appointment
     * @return Appointment type
     */
    public String getType() {
        return type;
    }

    /**
     * Gets starting time of appointment in UTC timezone and Timestamp format
     * @return Appointment start
     */
    public Timestamp getStart() {
        return start;
    }

    /**
     * Gets the appointment start time in local timezone
     * @return Start time/date of appointment in local timezone
     */
    public Timestamp getLocalStart() {
        return localStart;
    }

    /**
     * Gets the formatted local meeting start time in 12 hour, AM/PM format
     * @return Appointment starting time in local timezone and Timestamp format
     */
    public String getFormattedLocalStart() {
        return formattedLocalStart;
    }

    /**
     * Gets the appointment ending time in UTC timezone
     * @return Appointment ending time in UTC timezone and Timestamp format
     */
    public Timestamp getEnd() {
        return end;
    }

    /**
     * Gets the formatted local meeting end time in local timezone and Timestamp format
     * @return Appointment ending time in local timezone and Timestamp format
     */
    public Timestamp getLocalEnd() {
        return localEnd;
    }

    /**
     * Gets the formatted local meeting end time in 12 hour, AM/PM format
     * @return Appointment ending time in local timezone and Timestamp format
     */
    public String getFormattedLocalEnd() {
        return formattedLocalEnd;
    }

    /**
     * Gets date/time that appointment was created on
     * @return Date/time that appointment was created on
     */
    public Timestamp getCreateDate() {
        return createDate;
    }

    /**
     * Gets user that appointment was created by
     * @return User that appointment was created by
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Gets date/time that appointment was last updated by
     * @return Date/time that appointment was last updated by
     */
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Gets user that appointment was last updated by
     * @return User that appointment was last updated by
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * Gets ID number of user in appointment
     * @return User ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Gets ID number of contact in appointment
     * @return Contact ID
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Gets ID number of customer in appointment
     * @return Customer ID
     */
    public int getCustomerId() {
        return customerId;
    }
}