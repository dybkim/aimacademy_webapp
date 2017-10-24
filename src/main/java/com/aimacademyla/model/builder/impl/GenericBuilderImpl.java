package com.aimacademyla.model.builder.impl;

import com.aimacademyla.model.builder.GenericBuilder;
import com.aimacademyla.model.enums.AIMEntityType;
import com.aimacademyla.service.factory.ServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

public abstract class GenericBuilderImpl<T> implements GenericBuilder<T> {

    private ServiceFactory serviceFactory;

    GenericBuilderImpl(ServiceFactory serviceFactory){
        this.serviceFactory = serviceFactory;
    }

    public void setServiceFactory(ServiceFactory serviceFactory){
        this.serviceFactory = serviceFactory;
    }

    ServiceFactory getServiceFactory(){
        return serviceFactory;
    }

    @Override
    public abstract T build();
}
