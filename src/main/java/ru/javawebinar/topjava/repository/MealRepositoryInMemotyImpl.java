package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class MealRepositoryInMemotyImpl implements BasicCrudRepository<Meal, Long> {

    private final AtomicLong idCounter = new AtomicLong(1000);
    private final ConcurrentHashMap<Long, Meal> meals = new ConcurrentHashMap<>();

    public MealRepositoryInMemotyImpl() {

        for (Meal meal : MealsUtil.getTestMealList()) {
            save( meal);
        }

    }

    @Override
    public Optional<Meal> get(Long id) {
        if (id == 0) return Optional.empty();

        return Optional.ofNullable(meals.get(id));
    }


    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal save(Meal entity) {

        long id = entity.getId();
        Meal meal;
        if (id == 0L){
            id = idCounter.incrementAndGet();
            meal = new Meal(id,
                    entity.getDateTime(),
                    entity.getDescription(),
                    entity.getCalories());
        } else {
            meal = entity;
        }

        meals.put(id, meal);
        return meal;
    }

    @Override
    public void delete(Long id) {
        meals.remove(id);
    }
}
