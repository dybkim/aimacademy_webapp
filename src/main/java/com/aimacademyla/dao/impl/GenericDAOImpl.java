package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.model.id.IDGenerationStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * Created by davidkim on 6/23/17.
 */

@Repository("genericDAOImpl")
@Transactional
public abstract class GenericDAOImpl<T, K extends Serializable> implements GenericDAO<T, K> {

    private SessionFactory sessionFactory;
    private Class<T> entityClass;

    private static final Logger logger = LogManager.getLogger(GenericDAOImpl.class.getName());
    /**
     * Constructor must establish the entity type T at compile time because generics don't exist at runtime
     */
    public GenericDAOImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    Session currentSession(){
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void add(T entity) {
        Session session = currentSession();
        session.saveOrUpdate(entity);
        session.flush();
    }

    @Override
    public void update(T entity){
        Session session= currentSession();
        try{
            session.saveOrUpdate(entity);
        }catch(NonUniqueObjectException e){
            logger.debug("update " + entity + " failed due to NonUniqueObjectException, attempting merge!");
            session.merge(entity);
            logger.debug("update " + entity + " via merge successful!");
        }
        session.flush();
    }

    @Override
    public void remove(T entity) {
        Session session = currentSession();
        session.remove(entity);
        session.flush();
    }

    @Override
    public abstract void removeList(List<T> entityList);

    @Override
    @SuppressWarnings("unchecked")
    public T get(K key) {
        Session session = currentSession();
        Criteria criteria = session.createCriteria(entityClass);
        char c[] = entityClass.getSimpleName().toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        String propertyName = new String(c) + "ID";
        criteria.add(Restrictions.eq(propertyName, key));
        return (T) criteria.uniqueResult();
    }

    /*
     * Must be overridden to load lazy-loaded Collections
     */
    @Override
    public T getEager(K key){
        return get(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(List<Criterion> criterionList){
        Session session = currentSession();
        Criteria criteria = session.createCriteria(entityClass);
        for(Criterion criterion : criterionList)
            criteria.add(criterion);

        return (T) criteria.uniqueResult();
    }

    /**
     * getList() uses criteriaQuery instead of HQL due to the generic entity type of GenericDAO (HQL requires the actual entity type)
     * @return
     */
    @Override
    public List<T> getList(){
        Session session = currentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);
        return session.createQuery(criteriaQuery).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getList(List<Criterion> criterionList){
        Session session = currentSession();
        Criteria criteria = session.createCriteria(entityClass);
        for(Criterion criterion : criterionList)
            criteria.add(criterion);

        return criteria.list();
    }

    @Override
    public Class getEntityClass(){
        return entityClass;
    }

    @Override
    public T loadCollection(T entity, Class classType){return entity;}

    @Override
    public T loadCollections(T entity){return entity;}

    @Override
    public T loadSubcollections(T entity){return entity;}

    @Override
    public Collection<T> loadCollections(Collection<T> entityCollection){return entityCollection;}
}
