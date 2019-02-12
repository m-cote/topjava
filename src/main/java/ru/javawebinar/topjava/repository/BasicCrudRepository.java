package ru.javawebinar.topjava.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BasicCrudRepository<T, ID extends Serializable> {

    Optional<T> get(ID id);
    boolean exists(ID id);
    List<T> getAll();
    long count();
    T save(T entity);
    void delete(T entity);
}
