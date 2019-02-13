package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.BasicCrudRepository;
import ru.javawebinar.topjava.repository.MealRepositoryInMemotyImpl;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final int CALORIES_PER_DAY = 2000;
    private static final Logger log = getLogger(MealServlet.class);

    private final BasicCrudRepository repository = new MealRepositoryInMemotyImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) action = "";

        switch (action) {
            case "edit": {
                long id = Long.parseLong(req.getParameter("id"));
                Optional<Meal> optionalMeal = repository.get(id);
                if (optionalMeal.isPresent()) {
                    log.debug("forward to mealEdit");
                    req.setAttribute("meal", optionalMeal.get());
                    req.getRequestDispatcher("/mealEdit.jsp").forward(req, resp);
                }
            } break;

            case "delete": {
                long id = Long.parseLong(req.getParameter("id"));
                repository.delete( id);
                log.debug("redirect to meals");
                resp.sendRedirect("meals");

            } break;
            default: {

                log.debug("forward to meals");

                List<MealTo> mealToList = MealsUtil.getFilteredWithExcess(repository.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);

                req.setAttribute("dateFormat", TimeUtil.dateFormat);
                req.setAttribute("meals", mealToList);
                req.getRequestDispatcher("/meals.jsp").forward(req, resp);
            }
            break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        long id = Long.parseLong(req.getParameter("id"));

        if (req.getParameter("update") != null) {
            LocalDateTime ldt = LocalDateTime.parse(req.getParameter("dateTime"), ISO_LOCAL_DATE_TIME);
            String description = req.getParameter("description");
            int calories = Integer.parseInt( req.getParameter("calories"));

            repository.save( new Meal(id, ldt, description, calories));

        } else if (req.getParameter("delete") != null) {

            repository.delete( id);
        }

        log.debug("redirect to meals");
        resp.sendRedirect("meals");
    }


}
