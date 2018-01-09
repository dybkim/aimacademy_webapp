package com.aimacademyla.model.initializer.impl;

import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.initializer.DefaultValueInitializer;

/*
 * GenericDefaultValueInitializers must use DAO's because the service classes use the genericDefaultValueInitializers to create entity instances.
 */
public abstract class GenericDefaultValueInitializerImpl<T> implements DefaultValueInitializer<T> {

    private DAOFactory daoFactory;

    GenericDefaultValueInitializerImpl(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    public void setDAOFactory(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    DAOFactory getDAOFactory(){
        return daoFactory;
    }

    @Override
    public abstract T initialize();
}
