package com.aimacademyla.service;

import java.util.Collection;
import java.util.List;

/**
 * Created by davidkim on 6/23/17.
 */
public interface GenericService<T,K> {
    void add(T entity);
    void update(T entity);
    void remove(T entity);
    T get(K key);
    T getEager(K key);
    List<T> getList();
    Class<T> getEntityClass();
    T loadCollection(T entity, Class classType);
    T loadCollections(T entity);
    T loadSubcollections(T entity);
    Collection<T> loadCollections(Collection<T> entityCollection);
}
