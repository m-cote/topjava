package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.Util;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal get(int id, int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    @Override
    public void delete(int id, int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    @Override
    public List<Meal> getBetweenDateTimes(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        Assert.notNull(startDateTime, "startDateTime must not be null");
        Assert.notNull(endDateTime, "endDateTime  must not be null");
        return repository.getBetween(startDateTime, endDateTime, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    @Override
    public DataTablesOutput<MealTo> getAll(int userId, DataTablesInput input) {
        return getDataTablesOutputWithExcess(repository.getAll(userId, input), userId, meal -> true);
    }

    @Override
    public DataTablesOutput<MealTo> getBetweenDateTime(int userId, DataTablesInput input, LocalDateTime startDate, LocalTime startTime, LocalDateTime endDate, LocalTime endTime) {
        Assert.notNull(startDate, "startDate must not be null");
        Assert.notNull(startTime, "startTime  must not be null");
        Assert.notNull(endDate, "endDate must not be null");
        Assert.notNull(endTime, "endTime  must not be null");
        DataTablesOutput<Meal> mealsOutput = repository.getBetween(userId, input, startDate, endDate);
        return getDataTablesOutputWithExcess(mealsOutput, userId, meal -> Util.isBetween(meal.getTime(), startTime, endTime));
    }

    private DataTablesOutput<MealTo> getDataTablesOutputWithExcess(DataTablesOutput<Meal> output, int userId, Predicate<Meal> filter) {
        if (output.getError() != null) {
            throw new IllegalArgumentException(output.getError());
        }

        List<Meal> meals = output.getData().stream()
                .filter(filter)
                .collect(Collectors.toList());

        List<LocalDate> days = meals.stream()
                .map(meal -> meal.getDateTime().toLocalDate())
                .distinct()
                .collect(Collectors.toList());

        Map<LocalDate, Boolean> exceededByDays = repository.getExceededByDays(userId, days, SecurityUtil.authUserCaloriesPerDay());

        List<MealTo> mealTos = meals.stream()
                .map(meal -> MealsUtil.createWithExcess(meal, exceededByDays.get(meal.getDateTime().toLocalDate())))
                .collect(Collectors.toList());

        DataTablesOutput<MealTo> result = new DataTablesOutput<>();
        result.setData(mealTos);
        result.setDraw(output.getDraw());
        result.setError(output.getError());
        result.setRecordsFiltered(output.getRecordsFiltered());
        result.setRecordsTotal(output.getRecordsTotal());
        return result;
    }

    @Override
    public void update(Meal meal, int userId) {
        Assert.notNull(meal, "meal must not be null");
        checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }

    @Override
    public Meal create(Meal meal, int userId) {
        Assert.notNull(meal, "meal must not be null");
        return repository.save(meal, userId);
    }

    @Override
    public Meal getWithUser(int id, int userId) {
        return checkNotFoundWithId(repository.getWithUser(id, userId), id);
    }
}