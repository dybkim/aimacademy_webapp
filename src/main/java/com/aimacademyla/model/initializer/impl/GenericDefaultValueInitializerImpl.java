package com.aimacademyla.model.initializer.impl;

import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.initializer.DefaultValueInitializer;
import com.aimacademyla.util.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/*
 * GenericDefaultValueInitializers must use DAO's because the service classes use the genericDefaultValueInitializers to create entity instances.
 */
public abstract class GenericDefaultValueInitializerImpl<T> implements DefaultValueInitializer<T> {

    private DAOFactory daoFactory;

    GenericDefaultValueInitializerImpl(){
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        daoFactory = (DAOFactory)context.getBean("daoFactory");
    }

    protected DAOFactory getDAOFactory(){
        return daoFactory;
    }

    @Override
    public abstract T initialize();
}
