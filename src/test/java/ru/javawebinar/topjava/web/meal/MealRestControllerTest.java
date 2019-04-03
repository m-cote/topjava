package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.TestUtil.readFromJson;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;
import static ru.javawebinar.topjava.web.json.JsonUtil.writeValue;

public class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Autowired
    private MealService mealService;

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + MealTestData.MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEAL1));
    }

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(writeValue(MealsUtil.getWithExcess(MEALS, DEFAULT_CALORIES_PER_DAY))));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(mealService.getAll(USER_ID), MealsUtil.getWithExcess(List.of(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2), DEFAULT_CALORIES_PER_DAY));
    }

    @Test
    void testCreate() throws Exception {
        Meal expected = getCreated();
        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isCreated());

        Meal returned = readFromJson(action, Meal.class);
        expected.setId(returned.getId());

        assertMatch(returned, expected);
        assertMatch(mealService.getAll(USER_ID), MealsUtil.getWithExcess(List.of(expected, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1), DEFAULT_CALORIES_PER_DAY));
    }

    @Test
    void testUpdate() throws Exception {
        Meal updated = getUpdated();
        mockMvc.perform(put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        assertMatch(mealService.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    void testGetFiltered() throws Exception {
        mockMvc.perform(get(REST_URL + "filter?startDate=" + LocalDate.of(2015, Month.MAY, 30)
                + "&startTime=" + LocalTime.of(13, 0, 0)
                + "&endDate=" + LocalDate.of(2015, Month.MAY, 30)
                + "&endTime=" + LocalTime.MAX
        ))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(writeValue(MealsUtil.getWithExcess(List.of(MEAL3, MEAL2), DEFAULT_CALORIES_PER_DAY))));
    }

    @Test
    void testGetFilteredByDate() throws Exception {
        mockMvc.perform(get(REST_URL + "filter?startDate=" + LocalDate.of(2015, Month.MAY, 30)
                + "&endDate=" + LocalDate.of(2015, Month.MAY, 30)
        ))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(writeValue(MealsUtil.getWithExcess(List.of(MEAL3, MEAL2, MEAL1), DEFAULT_CALORIES_PER_DAY))));
    }

}
