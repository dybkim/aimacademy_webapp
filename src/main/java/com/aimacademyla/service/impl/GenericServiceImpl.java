package com.aimacademyla.service.impl;

import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public abstract class GenericServiceImpl<T, K> implements GenericService<T, K> {

    private GenericDAO<T,K> genericDAO;
    private DAOFactory daoFactory;
    private Class<T> entityClass;

    public GenericServiceImpl(GenericDAO<T, K> genericDAO){
        this.genericDAO = genericDAO;
        this.entityClass = genericDAO.getEntityClass();
    }

    @Autowired
    public void setDAOFactory(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    @Override
    public void add(T entity) {
        genericDAO.add(entity);
    }

    @Override
    public void update(T entity) {
        genericDAO.update(entity);
    }

    @Override
    public void remove(T entity) {
        genericDAO.remove(entity);
    }

    @Override
    public T get(K key) {
        return genericDAO.get(key);
    }

    @Override
    public T getEager(K key){return genericDAO.getEager(key);}

    @Override
    public List<T> getList(){
        return genericDAO.getList();
    }

    public DAOFactory getDAOFactory() {
        return daoFactory;
    }

    @Override
    public Class<T> getEntityClass(){
        return entityClass;
    }

    @Override
    public T loadCollection(T entity, Class classType){return genericDAO.loadCollection(entity, classType);}

    @Override
    public T loadCollections(T entity){return genericDAO.loadCollections(entity);}

    @Override
    public T loadSubcollections(T entity){return genericDAO.loadSubcollections(entity);}

    @Override
    public Collection<T> loadCollections(Collection<T> entityCollection){return genericDAO.loadCollections(entityCollection);}
}
