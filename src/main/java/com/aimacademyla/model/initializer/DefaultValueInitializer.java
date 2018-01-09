package com.aimacademyla.model.initializer;


/**
 * GenericDefaultValueInitializer initializes domain objects with empty values
 * @param <T>: Domain object to be initialized
 */
public interface DefaultValueInitializer<T> {
    T initialize();
}
