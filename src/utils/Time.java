package utils;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Commonly used time related methods
 */
public class Time {
    /**
     * Converts date, hour, and string input from form to a UTC-zone time in timestamp format
     * @param utcTime selected month/day for appointment in LocalDate format
     * @param hour selected hour for appointment in String format
     * @param minute selected minute for appointment in String format
     * @return UTC equivalent of local appointment time in Timestamp format
     */
    public Timestamp convertTimeToUTC(LocalDate utcTime, String hour, String minute) {
        LocalDateTime LDT = LocalDateTime.of(utcTime.getYear(), utcTime.getMonthValue(), utcTime.getDayOfMonth(), Integer.parseInt(hour), Integer.parseInt(minute));
        ZonedDateTime locZdt = ZonedDateTime.of(LDT, ZoneId.systemDefault());
        ZonedDateTime UtcZdt = locZdt.withZoneSameInstant(ZoneOffset.UTC);
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Timestamp.valueOf(UtcZdt.format(customFormatter));
    }

    /**
     * Converts date, hour, and string input from form to a local time in timestamp format
     * @param utcTime selected month/day for appointment in LocalDate format
     * @param hour selected hour for appointment in String format
     * @param minute selected minute for appointment in String format
     * @return Local appointment time in LocalDateTime format
     */
    public Timestamp convertTimeToLocal(LocalDate utcTime, String hour, String minute) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd KK:mm:ss a");
        LocalDateTime LDT = LocalDateTime.of(utcTime.getYear(), utcTime.getMonthValue(), utcTime.getDayOfMonth(), Integer.parseInt(hour), Integer.parseInt(minute));
        return Timestamp.valueOf(LDT);
    }
}
