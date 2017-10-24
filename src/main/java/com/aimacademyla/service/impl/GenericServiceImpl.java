package com.aimacademyla.service.impl;

import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.enums.AIMEntityType;
import com.aimacademyla.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by davidkim on 6/23/17.
 */

@Service
public abstract class GenericServiceImpl<E, K> implements GenericService<E, K> {

    private GenericDAO<E,K> genericDAO;
    private DAOFactory daoFactory;

    public GenericServiceImpl(GenericDAO<E, K> genericDAO){
        this.genericDAO = genericDAO;
    }

    @Autowired
    public void setDAOFactory(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    @Override
    public void add(E entity) {
        genericDAO.add(entity);
    }

    @Override
    public void update(E entity) {
        genericDAO.update(entity);
    }

    @Override
    public void remove(E entity) {
        genericDAO.remove(entity);
    }

    @Override
    public E get(K key) {
        return genericDAO.get(key);
    }

    @Override
    public List<E> getList(){
        return genericDAO.getList();
    }

    @Override
    public abstract AIMEntityType getAIMEntityType();

    public DAOFactory getDaoFactory() {
        return daoFactory;
    }
}
