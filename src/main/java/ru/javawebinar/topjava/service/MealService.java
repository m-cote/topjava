package ru.javawebinar.topjava.service;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.lang.Nullable;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.*;

public interface MealService {
    Meal get(int id, int userId) throws NotFoundException;

    void delete(int id, int userId) throws NotFoundException;

    default List<Meal> getBetweenDates(@Nullable LocalDate startDate, @Nullable LocalDate endDate, int userId) {
        return getBetweenDateTimes(adjustStartDateTime(startDate), adjustEndDateTime(endDate), userId);
    }

    default DataTablesOutput<MealTo> getBetweenDateTime(int userId, DataTablesInput input, @Nullable LocalDate startDate, @Nullable LocalTime startTime, @Nullable LocalDate endDate, @Nullable LocalTime endTime) {
        return getBetweenDateTime(userId, input, adjustStartDateTime(startDate), adjustStartTime(startTime), adjustEndDateTime(endDate), adjustEndTime(endTime));
    }

    List<Meal> getBetweenDateTimes(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId);

    DataTablesOutput<MealTo> getBetweenDateTime(int userId, DataTablesInput input, LocalDateTime startDate, LocalTime startTime, LocalDateTime endDate, LocalTime endTime);

    List<Meal> getAll(int userId);

    DataTablesOutput<MealTo> getAll(int userId, DataTablesInput input);

    void update(Meal meal, int userId) throws NotFoundException;

    Meal create(Meal meal, int userId);

    Meal getWithUser(int id, int userId);
}