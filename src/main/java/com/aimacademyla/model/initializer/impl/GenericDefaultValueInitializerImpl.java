package com.aimacademyla.model.initializer.impl;

import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.initializer.GenericDefaultValueInitializer;
import com.aimacademyla.service.factory.ServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * GenericDefaultValueInitializers must use DAO's because the service classes use the genericDefaultValueInitializers to create entity instances.
 * @param <T>
 */
public abstract class GenericDefaultValueInitializerImpl<T> implements GenericDefaultValueInitializer<T> {

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
