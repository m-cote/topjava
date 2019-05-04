package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends DataTablesRepository<Meal, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:userId")
    int delete(@Param("id") int id, @Param("userId") int userId);

    @Override
    @Transactional
    Meal save(Meal item);

    @Query("SELECT m FROM Meal m WHERE m.user.id=:userId ORDER BY m.dateTime DESC")
    List<Meal> getAll(@Param("userId") int userId);

    @Query("SELECT m FROM Meal m WHERE m.user.id=:userId AND m.description LIKE %:text%")
    Page<Meal> getAll(@Param("text") String text, Pageable pageable, @Param("userId") int userId);

    interface ExceededByDay {
        LocalDate getDay();
        Boolean getExceeded();
    }

    @Query(value = "SELECT  CAST(m.date_time AS DATE) AS DAY, CASE WHEN( SUM(m.calories) > :caloriesQuota) THEN TRUE ELSE FALSE END AS EXCEEDED " +
            "FROM meals m " +
            "WHERE m.user_id=:userId " +
            "      AND CAST(m.date_time AS DATE) IN :days " +
            "GROUP BY CAST(m.date_time AS DATE)", nativeQuery = true)
    List<ExceededByDay> getExcessByDays(@Param("userId") int userId, @Param("days") List<LocalDate> days, @Param("caloriesQuota") int caloriesQuota);

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT m from Meal m WHERE m.user.id=:userId AND m.dateTime BETWEEN :startDate AND :endDate ORDER BY m.dateTime DESC")
    List<Meal> getBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("userId") int userId);

    @Query("SELECT m from Meal m WHERE m.user.id=:userId AND m.dateTime BETWEEN :startDate AND :endDate AND m.description LIKE %:text% AND ((hour(m.dateTime) * 60)+minute(m.dateTime)) >= :startTime AND ((hour(m.dateTime) * 60)+minute(m.dateTime)) <= :endTime")
    Page<Meal> getBetween(@Param("text") String text, Pageable pageable, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("startTime") int startTime, @Param("endTime") int endTime, @Param("userId") int userId);

    @Query("SELECT m FROM Meal m JOIN FETCH m.user WHERE m.id = ?1 and m.user.id = ?2")
    Meal getWithUser(int id, int userId);
}