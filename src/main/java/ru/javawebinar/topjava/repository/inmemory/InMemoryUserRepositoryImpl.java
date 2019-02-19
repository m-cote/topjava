package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);
    private static Comparator<User> userComparator = new Comparator<User>() {
        @Override
        public int compare(User u1, User u2) {

            if (!Objects.equals(u1.getName(), u2.getName())) {
                return u1.getName().compareTo(u2.getName());
            } else if (!Objects.equals(u1.getRegistered(), u2.getRegistered())) {
                return u1.getRegistered().compareTo(u2.getRegistered());
            }

            return u1.getId().compareTo(u2.getId());
        }
    };

    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);

        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
            return user;
        }

        return repository.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");

        ArrayList<User> users = new ArrayList<>(repository.values());
        Collections.sort(users, userComparator);

        return users;
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);

        if (email == null) return null;
        return repository.values().stream()
                .filter(user -> email.equalsIgnoreCase(user.getEmail()))
                .findAny().orElse(null);
    }
}
