package ru.javawebinar.topjava.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.javawebinar.topjava.model.Meal;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;

public final class MealSpecifications {

    public static Specification<Meal> belongsToUser(int userId) {
        return (Specification<Meal>) (root, query, builder) -> builder.equal(root.get("user"), userId);
    }

    public static Specification<Meal> isWithinDateAndTime(LocalDateTime startDate, LocalDateTime endDate) {

        return (Specification<Meal>) (root, query, cb) -> {

            Expression<Integer> hour = cb.function("hour", Integer.class, root.get("dateTime"));
            Expression<Integer> minute = cb.function("minute", Integer.class, root.get("dateTime"));

            Predicate timeIsGreaterThanOrEqualToStartTime = cb.or(
                    cb.greaterThan(hour, startDate.getHour()),
                    cb.and(
                            cb.equal(hour, startDate.getHour()),
                            cb.greaterThanOrEqualTo(minute, startDate.getMinute())
                    )
            );
            Predicate timeIsLessThanOrEqualToEndTime = cb.or(
                    cb.lessThan(hour, endDate.getHour()),
                    cb.and(
                            cb.equal(hour, endDate.getHour()),
                            cb.lessThanOrEqualTo(minute, endDate.getMinute())
                    )
            );
            return cb.and(
                    cb.greaterThanOrEqualTo(root.get("dateTime"), startDate),
                    cb.lessThanOrEqualTo(root.get("dateTime"), endDate),
                    timeIsGreaterThanOrEqualToStartTime,
                    timeIsLessThanOrEqualToEndTime
            );
        };
    }

}
