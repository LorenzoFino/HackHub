package unicam.hackhub.domain.utils;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;

/**
 * Value object representing a date interval.
 * Used for the hackathon period (start and end date).
 */
@Embeddable
public record Period(LocalDate startDate, LocalDate endDate) {

    public Period {
        if (startDate == null || endDate == null)
            throw new IllegalArgumentException("Dates cannot be null");
        if (startDate.isAfter(endDate))
            throw new IllegalArgumentException("Start date cannot be after end date");
    }

    /** Returns true if the given date falls within this period */
    public boolean isWithinPeriod(LocalDate date) {
        return (date.isEqual(startDate) || date.isAfter(startDate)) &&
                (date.isEqual(endDate)   || date.isBefore(endDate));
    }

    /** Returns true if the given date is after the end of this period */
    public boolean isAfterPeriod(LocalDate date) {
        return date.isAfter(endDate);
    }

    /** Returns true if this period overlaps with another */
    public boolean overlapsWith(Period other) {
        return this.startDate.isBefore(other.endDate) &&
                other.startDate.isBefore(this.endDate);
    }
}