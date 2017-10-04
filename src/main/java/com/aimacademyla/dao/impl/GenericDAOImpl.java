package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.GenericDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by davidkim on 6/23/17.
 */

@Repository("genericDAOImpl")
@Transactional
public abstract class GenericDAOImpl<E, K extends Serializable> implements GenericDAO<E, K> {

    private SessionFactory sessionFactory;
    private Class<E> entityClass;
    private Type type;

    /**
     * Constructor must establish the E at compile time because generics don't exist at runtime
     */
    public GenericDAOImpl(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public Session currentSession(){
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void add(E entity) {
        Session session = currentSession();
        session.saveOrUpdate(entity);
    }

    @Override
    public void update(E entity){
        Session session = currentSession();
        session.saveOrUpdate(entity);
    }

    @Override
    public void remove(E entity) {
        Session session = currentSession();
        session.remove(entity);
    }

    @Override
    public E get(K key) {
        Session session = currentSession();
        return session.get(entityClass, key);
    }

    @Override
    public List<E> getList(){
        Session session = currentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<E> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);
        return session.createQuery(criteriaQuery).getResultList();
    }
}
