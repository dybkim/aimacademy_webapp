package com.aimacademyla.dao.factory;

import com.aimacademyla.dao.GenericDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("daoFactory")
public class DAOFactory{
    private List<GenericDAO> genericDAOList;

    private static final Map<Class, GenericDAO> genericDAOCache = new HashMap<>();

    @Autowired
    public DAOFactory(List<GenericDAO> genericDAOList) {
        this.genericDAOList = genericDAOList;
    }

    @PostConstruct
    public void initGenericDAOCache(){
        for(GenericDAO genericDAO : genericDAOList){
            genericDAOCache.put(genericDAO.getEntityClass(), genericDAO);
        }
    }

    public GenericDAO getDAO(Class entityClass) {
        return genericDAOCache.get(entityClass);
    }
}
