package com.tranner;

import java.time.*;
import java.util.ArrayList;

public interface LocationInfo {
    public void setOperationHours(ArrayList<LocalTime> hours);
    public void bookLocation(LocalDateTime start, LocalDateTime end, int numPersons);
    public boolean checkDiscounts(LocalDate date);
}
