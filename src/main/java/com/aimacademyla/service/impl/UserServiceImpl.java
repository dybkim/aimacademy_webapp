package com.aimacademyla.service.impl;

import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.model.User;
import com.aimacademyla.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends GenericServiceImpl<User, String> implements UserService{

    @Autowired
    public UserServiceImpl(@Qualifier("userDAO")GenericDAO<User, String> genericDAO) {
        super(genericDAO);
    }

}
