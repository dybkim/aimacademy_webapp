package com.aimacademyla.api.slack.service;

import com.aimacademyla.model.AIMEntityType;
import com.aimacademyla.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ServiceFactory {

    @Autowired
    private List<GenericService> genericServiceList;

    private static final Map<AIMEntityType, GenericService> genericServiceCache = new HashMap<>();

    @PostConstruct
    public void initGenericServiceCache(){
        for(GenericService genericService : genericServiceList){
            genericServiceCache.put(genericService.getAIMEntityType(), genericService);
        }
    }

    public GenericService getService(AIMEntityType entityType) {
        return genericServiceCache.get(entityType);
    }

    public void setGenericServiceList(List<GenericService> genericServiceList){
        this.genericServiceList = genericServiceList;
    }
}
