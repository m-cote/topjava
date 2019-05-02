package ru.javawebinar.topjava.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

public final class MealSpecifications {

    public static Specification<Meal> belongsToUser(int userId) {
        return (Specification<Meal>) (root, query, builder) -> builder.equal(root.get("user"), userId);
    }

    public static Specification<Meal> isWithinPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return (Specification<Meal>) (root, query, builder) -> builder.and(
                builder.greaterThanOrEqualTo(root.get("dateTime"), startDate),
                builder.lessThanOrEqualTo(root.get("dateTime"), endDate)
        );
    }

}
