package com.aimacademyla.service.impl;

import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.model.Authority;
import com.aimacademyla.service.AuthorityService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImpl extends GenericServiceImpl<Authority, String> implements AuthorityService{

    public AuthorityServiceImpl(@Qualifier("authorityDAO") GenericDAO<Authority, String> genericDAO) {
        super(genericDAO);
    }

}
