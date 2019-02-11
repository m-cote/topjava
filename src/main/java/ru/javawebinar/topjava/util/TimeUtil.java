package ru.javawebinar.topjava.util;

import java.time.LocalTime;

public class TimeUtil {

    public static final String dateFormat = "dd.MM.yyyy HH:mm";

    public static boolean isBetween(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) <= 0;
    }
}
