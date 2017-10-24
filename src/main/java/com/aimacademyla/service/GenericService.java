package com.aimacademyla.service;

import com.aimacademyla.model.enums.AIMEntityType;

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
    AIMEntityType getAIMEntityType();
}
