package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        List<UserMealWithExceed> filteredWithExceeded = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        for (UserMealWithExceed userMealWithExceed : filteredWithExceeded) {
            System.out.println(userMealWithExceed);
        }
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        //return getFilteredWithExceeded_Loops( mealList, startTime, endTime, caloriesPerDay);
        return getFilteredWithExceeded_Streams( mealList, startTime, endTime, caloriesPerDay);
    }

    private static List<UserMealWithExceed> getFilteredWithExceeded_Loops(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay){
        Map<LocalDate, Long> caloriesInDay = new HashMap<>();
        for (UserMeal userMeal : mealList) {
            LocalDate localDate = userMeal.getDateTime().toLocalDate();
            Long calories = caloriesInDay.getOrDefault(localDate, 0L);
            caloriesInDay.put(localDate,calories + userMeal.getCalories());
        }

        List<UserMealWithExceed> filtered = new ArrayList<>();
        for (UserMeal userMeal : mealList) {

            LocalDateTime dateTime = userMeal.getDateTime();
            if ( !TimeUtil.isBetween(dateTime.toLocalTime(), startTime, endTime) )
                continue;

            filtered.add( new UserMealWithExceed(
                    dateTime,
                    userMeal.getDescription(),
                    userMeal.getCalories(),
                    caloriesInDay.getOrDefault(dateTime.toLocalDate(),0L) > caloriesPerDay)
            );
        }

        return filtered;
    }

    private static List<UserMealWithExceed> getFilteredWithExceeded_Streams(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay){

        Map<LocalDate, Integer> caloriesInDay = mealList.stream()
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories))
                );

        return mealList.stream()
                .filter( userMeal -> TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> new UserMealWithExceed(
                        userMeal.getDateTime(),
                        userMeal.getDescription(),
                        userMeal.getCalories(),
                        caloriesInDay.getOrDefault(userMeal.getDateTime().toLocalDate(), 0) > caloriesPerDay
                        ))
                .collect(Collectors.toList());
    }
}
