package com.aimacademyla.service.factory;

import com.aimacademyla.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("serviceFactory")
public class ServiceFactory {

    private List<GenericService> genericServiceList;

    private static final Map<Class, GenericService> genericServiceCache = new HashMap<>();

    @Autowired
    public ServiceFactory(List<GenericService> genericServiceList) {
        this.genericServiceList = genericServiceList;
    }

    @PostConstruct
    public void initGenericServiceCache(){
        for(GenericService genericService : genericServiceList){
            genericServiceCache.put(genericService.getEntityClass(), genericService);
        }
    }

    public GenericService getService(Class classType) {
        return genericServiceCache.get(classType);
    }
}
