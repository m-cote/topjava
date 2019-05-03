package ru.javawebinar.topjava.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import ru.javawebinar.topjava.model.User;

import java.util.List;

public interface UserRepository {
    User save(User user);

    // false if not found
    boolean delete(int id);

    // null if not found
    User get(int id);

    // null if not found
    User getByEmail(String email);

    List<User> getAll();

    default Page<User> getPageable(String text,
                                   int page,
                                   int size,
                                   String sortBy,
                                   String direction) {
        throw new UnsupportedOperationException();
    }

    default DataTablesOutput<User> getAll(DataTablesInput input) {
        throw new UnsupportedOperationException();
    }

    default User getWithMeals(int id) {
        throw new UnsupportedOperationException();
    }
}