package com.aimacademyla.dao.flow;

import java.util.List;

/**
 * DAOAccessFlow is used to retrieve or instantiate Entity instances
 * @param <T>: Entity Class type
 */
public interface DAOAccessFlow <T>{
    T get();
    List<T> getList();
    DAOAccessFlow addQueryParameter(Object object);
    Object getQueryParameter(Class classType);
}
