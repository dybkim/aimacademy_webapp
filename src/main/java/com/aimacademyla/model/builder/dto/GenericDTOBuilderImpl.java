package com.aimacademyla.model.builder.dto;

import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.builder.GenericBuilder;
import com.aimacademyla.model.enums.AIMEntityType;
import com.aimacademyla.service.factory.ServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

public abstract class GenericDTOBuilderImpl<T> implements GenericBuilder<T> {

    private DAOFactory daoFactory;

    GenericDTOBuilderImpl(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    public void setDAOFactory(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    DAOFactory getDAOFactory(){
        return daoFactory;
    }

    @Override
    public abstract T build();
}
