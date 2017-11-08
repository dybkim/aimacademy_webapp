package com.aimacademyla.service.impl;

import com.aimacademyla.dao.AuthorityDAO;
import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.model.Authority;
import com.aimacademyla.model.enums.AIMEntityType;
import com.aimacademyla.service.AuthorityService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImpl extends GenericServiceImpl<Authority, String> implements AuthorityService{

    private AuthorityDAO authorityDAO;

    private final AIMEntityType AIM_ENTITY_TYPE = AIMEntityType.AUTHORITY;

    public AuthorityServiceImpl(@Qualifier("authorityDAO") GenericDAO<Authority, String> genericDAO) {
        super(genericDAO);
        this.authorityDAO = (AuthorityDAO) genericDAO;
    }

    @Override
    public AIMEntityType getAIMEntityType() {
        return AIM_ENTITY_TYPE;
    }

}
