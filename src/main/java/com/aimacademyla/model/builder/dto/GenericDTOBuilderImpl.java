package com.aimacademyla.model.builder.dto;

import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.builder.GenericBuilder;
import com.aimacademyla.util.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

public abstract class GenericDTOBuilderImpl<T> implements GenericBuilder<T> {

    private DAOFactory daoFactory;

    GenericDTOBuilderImpl(){
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        daoFactory = (DAOFactory)context.getBean("daoFactory");
    }

    DAOFactory getDAOFactory(){
        return daoFactory;
    }

    @Override
    public abstract T build();
}
