package ru.javawebinar.topjava.util;

import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

public class SortingUtil {

    private SortingUtil() {
    }

    public static Sort getSortFromString(String sortBy, String direction, Sort defaultSort) {

        if (sortBy.isEmpty() && direction.isEmpty()) return defaultSort;

        Sort.Direction dir = !direction.isEmpty() ? Sort.Direction.fromString(direction) : Sort.DEFAULT_DIRECTION;

        if (!sortBy.isEmpty()) {
            return Sort.by(dir, sortBy);
        } else {

            List<Sort.Order> orderList = defaultSort.stream()
                    .map(o -> o.with(dir))
                    .collect(Collectors.toList());

            return Sort.by(orderList);
        }
    }
}
