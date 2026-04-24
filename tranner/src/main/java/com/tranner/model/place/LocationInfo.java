package com.tranner.model.place;

import java.time.*;
import java.util.List;

public interface LocationInfo {

    /**
     * Sets or replaces the opeartion hours for this location.
     * @param hours
     */
    void setOperationalHours(List<LocalTime> hours);

    /**
     * Books this location for the specified time period.
     * @param startDate
     * @param endDate
     * @param startTime
     * @param endTime
     * @param numPersons
     * @return true if the booking was successful
     */
    boolean bookLocation(LocalDate startDate, LocalDate endDate,
                         LocalTime startTime, LocalTime endTime,
                         int numPersons);
    /**
     * Checks if any discounts are available for the specified date.
     * @param date
     * @return
     */
    String checkDiscounts(LocalDate date);
}
