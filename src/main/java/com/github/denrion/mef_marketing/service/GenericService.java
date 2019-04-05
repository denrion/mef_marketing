package com.github.denrion.mef_marketing.service;

import java.util.List;
import java.util.Optional;

public interface GenericService<T> {

    Optional<T> getById(Long id);

    List<T> getAll();

    T save(T t);

    Optional<T> update(T t, Long id);

    void delete(Long id);
}
