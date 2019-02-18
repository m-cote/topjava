package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.MealServiceImpl;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;
import ru.javawebinar.topjava.web.user.ProfileRestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ROLE_ADMIN));

            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            //mealRestController.delete(5);
            for (MealTo mealTo : mealRestController.getAll()) {
                System.out.println(mealTo);
            }
            System.out.println("==============================================");

            for (MealTo mealTo : mealRestController.getAllByDateTime(LocalDate.of(2015,5, 31),
                                                            null,
                                                            LocalTime.of(13, 00),
                                                            null)
                ) {
                System.out.println(mealTo);
            }
            System.out.println("==============================================");
            ProfileRestController userController = appCtx.getBean(ProfileRestController.class);
            User user = userController.create(new User(null, "userName", "email@mail.ru", "password", Role.ROLE_USER));

            MealServiceImpl mealService = appCtx.getBean(MealServiceImpl.class);
            mealService.delete(user.getId(), 5);
        }
    }
}
