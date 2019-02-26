package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-jdbc.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(USER_MEAL_ID, USER_ID);
        assertMatch(meal, USER_MEAL1);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(ADMIN_MEAL_ID, USER_ID);
    }

    @Test
    public void delete() {
        service.delete(USER_MEAL_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), USER_MEAL6, USER_MEAL5, USER_MEAL4, USER_MEAL3, USER_MEAL2);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        service.delete(ADMIN_MEAL_ID, USER_ID);
    }

    @Test
    public void getBetweenDates() {
        List<Meal> meals = service.getBetweenDates(
                LocalDate.of(2019, 02, 26),
                LocalDate.of(2019, 02, 26),
                USER_ID);

        assertMatch(meals, USER_MEAL6, USER_MEAL5, USER_MEAL4);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, USER_MEAL6, USER_MEAL5, USER_MEAL4, USER_MEAL3, USER_MEAL2, USER_MEAL1);
    }

    @Test
    public void update() {

        Meal updated = new Meal(USER_MEAL1);
        updated.setDescription("updated description");
        updated.setCalories(999);
        updated.setDateTime(LocalDateTime.of(2019, 02, 26, 0, 0));
        service.update(updated, USER_ID);
        assertMatch(service.get(USER_MEAL_ID, USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {

        Meal updated = new Meal(ADMIN_MEAL);
        updated.setDescription("updated description");
        updated.setCalories(999);
        updated.setDateTime(LocalDateTime.of(2019, 02, 26, 0, 0));
        service.update(updated, USER_ID);
    }

    @Test
    public void create() {

        Meal newMeal = new Meal(LocalDateTime.of(2019, 02, 26, 0, 0), "new meal", 999);
        Meal createdMeal = service.create(newMeal, ADMIN_ID);
        newMeal.setId(createdMeal.getId());
        assertMatch(service.getAll(ADMIN_ID), newMeal, ADMIN_MEAL);

    }
}