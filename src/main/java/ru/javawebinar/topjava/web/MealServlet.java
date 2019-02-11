package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("forward to meals");

        final int caloriesPerDay = 2000;
        List<MealTo> mealToList = MealsUtil.getFilteredWithExcess( MealsUtil.getTestMealList(), LocalTime.MIN, LocalTime.MAX, caloriesPerDay);

        req.setAttribute("dateFormat", TimeUtil.dateFormat);
        req.setAttribute("meals", mealToList);
        req.getRequestDispatcher("/meals.jsp").forward(req,resp);

    }



}
