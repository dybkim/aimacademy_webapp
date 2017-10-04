package com.aimacademyla.service;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by davidkim on 6/23/17.
 */
public interface GenericService<E,K> {
    void add(E entity);
    void update(E entity);
    void remove(E entity);
    E get(K key);
    List<E> getList();
}
