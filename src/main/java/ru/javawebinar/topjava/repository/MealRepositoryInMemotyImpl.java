package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class MealRepositoryInMemotyImpl implements MealRepository {

    private final static AtomicLong idCounter = new AtomicLong(1000);
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
    public boolean exists(Long id) {
        if (id == 0) return false;

        return get(id).isPresent();
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public long count() {
        return meals.size();
    }

    @Override
    public Meal save(Meal entity) {

        long id = entity.getId();
        if (id == 0L){
            id = idCounter.incrementAndGet();
            entity = new Meal(id,
                    entity.getDateTime(),
                    entity.getDescription(),
                    entity.getCalories());
        }

        meals.put(id, entity);
        return entity;
    }

    @Override
    public void delete(Meal entity) {
        meals.remove(entity.getId());
    }
}
