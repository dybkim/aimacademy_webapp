package com.aimacademyla.dao.factory;

import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.model.enums.AIMEntityType;
import com.aimacademyla.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DAOFactory {
    private List<GenericDAO> genericDAOList;

    private static final Map<AIMEntityType, GenericDAO> genericDAOCache = new HashMap<>();

    @Autowired
    public DAOFactory(List<GenericDAO> genericDAOList) {
        this.genericDAOList= genericDAOList;
    }

    @PostConstruct
    public void initGenericDAOCache(){
        for(GenericDAO genericDAO : genericDAOList){
            genericDAOCache.put(genericDAO.getAIMEntityType(), genericDAO);
        }
    }

    public GenericDAO getDAO(AIMEntityType entityType) {
        return genericDAOCache.get(entityType);
    }
}
