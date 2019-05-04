package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.specifications.MealSpecifications;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.SortingUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class DataJpaMealRepositoryImpl implements MealRepository {
    private static final Sort SORT_DATETIME = new Sort(Sort.Direction.DESC, "dateTime");

    @Autowired
    private CrudMealRepository crudMealRepository;

    @Autowired
    private CrudUserRepository crudUserRepository;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (!meal.isNew() && get(meal.getId(), userId) == null) {
            return null;
        }
        meal.setUser(crudUserRepository.getOne(userId));
        return crudMealRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudMealRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudMealRepository.findById(id).filter(meal -> meal.getUser().getId() == userId).orElse(null);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudMealRepository.getAll(userId);
    }

    @Override
    public DataTablesOutput<Meal> getAll(int userId, DataTablesInput input) {
        return crudMealRepository.findAll(input, null, MealSpecifications.belongsToUser(userId));
    }

    @Override
    public Page<Meal> getPageable(int userId, String text, int page, int size, String sortBy, String direction) {
        Sort sort = SortingUtil.getSortFromString(sortBy, direction, SORT_DATETIME);
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return crudMealRepository.getAll(text, pageRequest, userId);
    }


    @Override
    public Map<LocalDate, Boolean> getExceededByDays(int userId, List<LocalDate> days, int caloriesQuota) {

        if (!days.isEmpty()) {
            List<CrudMealRepository.ExceededByDay> resultSet = crudMealRepository.getExcessByDays(userId, days, caloriesQuota);
            if (resultSet != null) {
                return resultSet.stream()
                        .collect(
                                Collectors.toMap(CrudMealRepository.ExceededByDay::getDay, CrudMealRepository.ExceededByDay::getExceeded)
                        );
            }
        }
        return new HashMap<>();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return crudMealRepository.getBetween(startDate, endDate, userId);
    }

    @Override
    public DataTablesOutput<Meal> getBetween(int userId, DataTablesInput input, LocalDateTime startDate, LocalDateTime endDate) {
        return crudMealRepository.findAll(input, MealSpecifications.isWithinDateAndTime(startDate, endDate), MealSpecifications.belongsToUser(userId));
    }

    @Override
    public Page<Meal> getBetween(int userId, String text, int page, int size, String sortBy, String direction, LocalDateTime startDate, LocalDateTime endDate) {
        Sort sort = SortingUtil.getSortFromString(sortBy, direction, SORT_DATETIME);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return crudMealRepository.getBetween(text, pageRequest, startDate, endDate, DateTimeUtil.minutesSinceMidnight(startDate), DateTimeUtil.minutesSinceMidnight(endDate), userId);
    }

    @Override
    public Meal getWithUser(int id, int userId) {
        return crudMealRepository.getWithUser(id, userId);
    }
}
