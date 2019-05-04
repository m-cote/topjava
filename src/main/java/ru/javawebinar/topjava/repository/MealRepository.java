package ru.javawebinar.topjava.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface MealRepository {
    // null if updated meal do not belong to userId
    Meal save(Meal meal, int userId);

    // false if meal do not belong to userId
    boolean delete(int id, int userId);

    // null if meal do not belong to userId
    Meal get(int id, int userId);

    // ORDERED dateTime desc
    List<Meal> getAll(int userId);

    default DataTablesOutput<Meal> getAll(int userId, DataTablesInput input) {
        throw new UnsupportedOperationException();
    }

    default Page<Meal> getPageable(int userId, String text, int page, int size, String sortBy, String direction) {
        throw new UnsupportedOperationException();
    }

    default Map<LocalDate, Boolean> getExceededByDays(int userId, List<LocalDate> days, int caloriesQuota) {
        throw new UnsupportedOperationException();
    }

    // ORDERED dateTime desc
    List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId);

    default DataTablesOutput<Meal> getBetween(int userId, DataTablesInput input, LocalDateTime startDate, LocalDateTime endDate) {
        throw new UnsupportedOperationException();
    }

    default Page<Meal> getBetween(int userId, String text, int page, int size, String sortBy, String direction,
                                  LocalDateTime startDate, LocalDateTime endDate) {
        throw new UnsupportedOperationException();
    }

    default Meal getWithUser(int id, int userId) {
        throw new UnsupportedOperationException();
    }
}
