package com.aimacademyla.service.impl;

import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Created by davidkim on 6/23/17.
 */

@Service
public abstract class GenericServiceImpl<E, K> implements GenericService<E, K> {

    private GenericDAO<E,K> genericDAO;

    public GenericServiceImpl(GenericDAO<E, K> genericDAO){
        this.genericDAO = genericDAO;
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
}
