package ru.javawebinar.topjava.service;

import org.springframework.data.domain.Page;
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

import static ru.javawebinar.topjava.util.DateTimeUtil.adjustEndDateTime;
import static ru.javawebinar.topjava.util.DateTimeUtil.adjustStartDateTime;

public interface MealService {
    Meal get(int id, int userId) throws NotFoundException;

    void delete(int id, int userId) throws NotFoundException;

    default List<Meal> getBetweenDates(@Nullable LocalDate startDate, @Nullable LocalDate endDate, int userId) {
        return getBetweenDateTimes(adjustStartDateTime(startDate), adjustEndDateTime(endDate), userId);
    }

    default DataTablesOutput<MealTo> getBetweenDateTime(int userId, DataTablesInput input, @Nullable LocalDate startDate, @Nullable LocalDate endDate, @Nullable LocalTime startTime, @Nullable LocalTime endTime) {
        return getBetweenDateTime(userId, input, adjustStartDateTime(startDate, startTime), adjustEndDateTime(endDate, endTime));
    }

    default Page<MealTo> getBetweenDateTime(int userId, String text, int page, int size, String sortBy, String direction, @Nullable LocalDate startDate, @Nullable LocalDate endDate, @Nullable LocalTime startTime, @Nullable LocalTime endTime) {
        return getBetweenDateTime(userId, text, page, size, sortBy, direction, adjustStartDateTime(startDate, startTime), adjustEndDateTime(endDate, endTime));
    }

    List<Meal> getBetweenDateTimes(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId);

    DataTablesOutput<MealTo> getBetweenDateTime(int userId, DataTablesInput input, LocalDateTime startDate, LocalDateTime endDate);

    Page<MealTo> getBetweenDateTime(int userId, String text, int page, int size, String sortBy, String direction, LocalDateTime startDate, LocalDateTime endDate);

    List<Meal> getAll(int userId);

    Page<MealTo> getPageable(int userId, String text, int page, int size, String sortBy, String direction);

    DataTablesOutput<MealTo> getAll(int userId, DataTablesInput input);

    void update(Meal meal, int userId) throws NotFoundException;

    Meal create(Meal meal, int userId);

    Meal getWithUser(int id, int userId);
}