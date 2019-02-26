package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;

    public static final int ADMIN_MEAL_ID = START_SEQ + 2;
    public static final int USER_MEAL_ID = START_SEQ + 3;

    public static final Meal ADMIN_MEAL = new Meal(ADMIN_MEAL_ID, LocalDateTime.of(2019,02,25,8,5),"admin meal",1500);
    public static final Meal USER_MEAL1 = new Meal(USER_MEAL_ID, LocalDateTime.of(2019,02,25,8,0),"user meal 1",1000);
    public static final Meal USER_MEAL2 = new Meal(USER_MEAL_ID+1, LocalDateTime.of(2019,02,25,12,0),"user meal 2",250);
    public static final Meal USER_MEAL3 = new Meal(USER_MEAL_ID+2, LocalDateTime.of(2019,02,25,21,0),"user meal 3",750);
    public static final Meal USER_MEAL4 = new Meal(USER_MEAL_ID+3, LocalDateTime.of(2019,02,26,8,0),"user meal 4",1000);
    public static final Meal USER_MEAL5 = new Meal(USER_MEAL_ID+4, LocalDateTime.of(2019,02,26,12,0),"user meal 5",250);
    public static final Meal USER_MEAL6 = new Meal(USER_MEAL_ID+5, LocalDateTime.of(2019,02,26,21,0),"user meal 6",750);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }

}
