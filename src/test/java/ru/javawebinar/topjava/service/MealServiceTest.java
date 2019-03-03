package ru.javawebinar.topjava.service;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    private static final Logger log = getLogger(MealServiceTest.class);

    @Autowired
    private MealService service;

    @ClassRule
    public static final ClassTimerRule classTimer = new ClassTimerRule();

    private static class ClassTimerRule extends ExternalResource {

        private StringBuilder testResults;

        void addTestResult(String testResult) {
            testResults.append(testResult);
        }

        @Override
        protected void before() throws Throwable {
            testResults = new StringBuilder();
            log.info("\n======================================= MEAL SERVICE TESTS STARTED =======================================");
        }

        @Override
        protected void after() {

            log.info("\n======================================= MEAL SERVICE TESTS FINISHED =======================================" +
                    testResults.toString() +
                    "\n===========================================================================================================");
        }
    }


    @Rule
    public TestRule timer = new TestWatcher() {

        private long startTime;

        @Override
        protected void starting(Description description) {
            super.starting(description);
            String methodName = description.getMethodName();
            startTime = System.currentTimeMillis();
            log.info("\n--------------------------------------- test " + methodName + " started ---------------------------------------");
        }

        @Override
        protected void finished(Description description) {
            super.finished(description);
            String methodName = description.getMethodName();
            long totalTime = System.currentTimeMillis() - startTime;
            String testResult = "\n" + String.format("%-20s", methodName) + " - " + String.format("%8.3f", totalTime / 1000.0) + " s";
            classTimer.addTestResult(testResult);
            log.info("\n--------------------------------------- test " + methodName + " finished ---------------------------------------" +
                    testResult +
                    "\n--------------------------------------------------------------------------------------------------------------");
        }
    };

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void delete() throws Exception {
        service.delete(MEAL1_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    public void deleteNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("Not found entity with id=" + MEAL1_ID);
        service.delete(MEAL1_ID, 1);
    }

    @Test
    public void create() throws Exception {
        Meal newMeal = getCreated();
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(newMeal, created);
        assertMatch(service.getAll(USER_ID), newMeal, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void get() throws Exception {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(actual, ADMIN_MEAL1);
    }

    @Test
    public void getNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("Not found entity with id=" + MEAL1_ID);
        service.get(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void update() throws Exception {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    public void updateNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("Not found entity with id=" + MEAL1_ID);
        service.update(MEAL1, ADMIN_ID);
    }

    @Test
    public void getAll() throws Exception {
        assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    public void getBetween() throws Exception {
        assertMatch(service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30), USER_ID), MEAL3, MEAL2, MEAL1);
    }
}