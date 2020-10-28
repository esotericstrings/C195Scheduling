package Model;

import javafx.beans.value.ObservableValue;
/**
 * Represents a short summary report
 * of appointment data
 */
public class Report {
    /**
     * Number of appointments with the attribute of interest for the report
     */
    private ObservableValue<Integer> count;
    /**
     * Attribute of interest being reported on
     */
    private ObservableValue<String> attribute;

    /**
     * Creates report object with provided reportable attribute and appointment count
     * @param count Number of appointments with the attribute of interest for the report
     * @param attribute Attribute of interest being reported on
     */
    public Report(ObservableValue<Integer> count, ObservableValue<String> attribute) {
        super();
        setCount(count);
        setAttribute(attribute);
    }

    /**
     * Sets number of appointments that match reported attribute
     * @param count Number of appointments with the attribute of interest for the report
     */
    public void setCount(ObservableValue<Integer> count) {
        this.count = count;
    }

    /**
     * Sets attribute of appointment being reported on
     * @param attribute Attribute of interest being reported on
     */
    public void setAttribute(ObservableValue<String> attribute) {
        this.attribute = attribute;
    }

    /**
     * Gets count of appointments that match attribute
     * @return Number of appointments with the attribute of interest for the report
     */
    public ObservableValue<Integer> getCount() {
        return count;
    }

    /**
     * Gets attribute of appointment being reported
     * @return Attribute of interest being reported on
     */
    public ObservableValue<String> getAttribute() {
        return attribute;
    }
}