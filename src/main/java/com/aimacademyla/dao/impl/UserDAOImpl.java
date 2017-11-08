package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.UserDAO;
import com.aimacademyla.model.User;
import com.aimacademyla.model.enums.AIMEntityType;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("userDAO")
@Transactional
public class UserDAOImpl extends GenericDAOImpl<User, String> implements UserDAO{

    private final AIMEntityType AIM_ENTITY_TYPE = AIMEntityType.USER;

    public UserDAOImpl(){
        super(User.class);
    }


    @Override
    public AIMEntityType getAIMEntityType() {
        return AIM_ENTITY_TYPE;
    }
}
