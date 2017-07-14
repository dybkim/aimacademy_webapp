package com.aimacademyla.dao;

/**
 * Created by davidkim on 6/23/17.
 */
public interface GenericDAO <T,K> {
    void add(T entity);
    void update(T entity);
    void remove(T entity);
    T get(K key);
}
