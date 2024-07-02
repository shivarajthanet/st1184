package com.shiva.rental_service.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

/**
 * This class provides functionality to determine if a given date is a holiday.
 * It includes methods to check if a date is Independence Day or Labor Day.
 */
public class HolidayService {

    /**
     * Determines if the given date is a holiday.
     *
     * @param date the date to check
     * @return true if the date is Independence Day or Labor Day, false otherwise
     */
    public boolean isHoliday(LocalDate date) {
        int year = date.getYear();
        LocalDate independenceDay = getIndependenceDay(year);
        LocalDate laborDay = getLaborDay(year);

        return date.equals(independenceDay) || date.equals(laborDay);
    }

    /**
     * A method to get the independence day for a given year.
     *
     * @param year the year for which the independence day is needed
     * @return the calculated independence day adjusted for weekends
     */
    private LocalDate getIndependenceDay(int year) {
        LocalDate independenceDay = LocalDate.of(year, Month.JULY, 4);
        DayOfWeek dayOfWeek = independenceDay.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY) {
            return independenceDay.minusDays(1);
        } else if (dayOfWeek == DayOfWeek.SUNDAY) {
            return independenceDay.plusDays(1);
        }
        return independenceDay;
    }

    /**
     * A method to get the date of Labor Day for a given year.
     *
     * @param year the year for which Labor Day date is needed
     * @return the date of Labor Day for the specified year
     */
    private LocalDate getLaborDay(int year) {
        return LocalDate.of(year, Month.SEPTEMBER, 1).with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
    }
}
