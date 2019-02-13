package ru.javawebinar.topjava.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BasicCrudRepository<T, ID extends Serializable> {

    Optional<T> get(ID id);
    List<T> getAll();
    T save(T entity);
    void delete(ID id);
}
