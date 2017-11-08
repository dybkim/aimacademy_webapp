package com.aimacademyla.service.impl;

import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.dao.UserDAO;
import com.aimacademyla.model.User;
import com.aimacademyla.model.enums.AIMEntityType;
import com.aimacademyla.service.GenericService;
import com.aimacademyla.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends GenericServiceImpl<User, String> implements UserService{

    private UserDAO userDAO;

    private final AIMEntityType AIM_ENTITY_TYPE = AIMEntityType.SEASON;

    @Autowired
    public UserServiceImpl(@Qualifier("userDAO")GenericDAO<User, String> genericDAO) {
        super(genericDAO);
        userDAO = (UserDAO) genericDAO;
    }

    @Override
    public AIMEntityType getAIMEntityType() {
        return AIM_ENTITY_TYPE;
    }
}
