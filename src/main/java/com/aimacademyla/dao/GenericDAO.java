package com.aimacademyla.dao;

import org.hibernate.criterion.Criterion;

import java.util.Collection;
import java.util.List;

/**
 * Created by davidkim on 6/23/17.
 */
public interface GenericDAO <T,K> {
    void add(T entity);
    void update(T entity);
    void remove(T entity);
    void removeList(List<T> entityList);
    T get(K key);
    T getEager(K key);
    T get(List<Criterion> criteria);
    List<T> getList();
    List<T> getList(List<Criterion> criteria);
    Class getEntityClass();
    T loadCollection(T entity, Class classType);
    T loadCollections(T entity);
    T loadSubcollections(T entity);
    Collection<T> loadCollections(Collection<T> entityCollection);
}
