package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);

    private Map<Integer,Map<Integer, Meal>> repository = new HashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        for (int userId = 1; userId <= 2; userId++) {
            for (Meal testMeal : MealsUtil.getTestMeals()) {
                save(userId, testMeal);
            }
        }

    }

    @Override
    public Meal save(int userId, Meal meal) {
        log.info("save {}", meal);

        Map<Integer, Meal> userMeals = getUserMeals(userId);

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            userMeals.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but absent in storage
        return userMeals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        log.info("delete {}", id);

        Map<Integer, Meal> userMeals = getUserMeals(userId);
        return userMeals.remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        log.info("get {}", id);

        Map<Integer, Meal> userMeals = getUserMeals(userId);
        return userMeals.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll");
        Map<Integer, Meal> userMeals = getUserMeals(userId);

        return getAllFiltered( userMeals, meal -> true);
    }

    @Override
    public List<Meal> getAllByDate(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getAllByDate from {} to {}", startDate, endDate);

        Map<Integer, Meal> userMeals = getUserMeals(userId);

        return getAllFiltered( userMeals, meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate));
    }

    private List<Meal> getAllFiltered( Map<Integer, Meal> userMeals, Predicate<Meal> mealPredicate){
        return userMeals.values().stream()
                .filter(mealPredicate)
                .sorted(Comparator.comparing(Meal::getDateTime,Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    private Map<Integer, Meal> getUserMeals(int userId){
        return repository.computeIfAbsent(userId, user -> new HashMap<>());
    }

}

