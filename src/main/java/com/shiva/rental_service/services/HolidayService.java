package com.shiva.rental_service.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.HashSet;
import java.util.Set;

public class HolidayService {
    private final Set<LocalDate> holidays;

    public HolidayService() {
        this.holidays = new HashSet<>();
    }

    public boolean isHoliday(LocalDate date) {
        int year = date.getYear();
        LocalDate independenceDay = getIndependenceDay(year);
        LocalDate laborDay = getLaborDay(year);

        return date.equals(independenceDay) || date.equals(laborDay);
    }

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

    private LocalDate getLaborDay(int year) {
        return LocalDate.of(year, Month.SEPTEMBER, 1).with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
    }
}
