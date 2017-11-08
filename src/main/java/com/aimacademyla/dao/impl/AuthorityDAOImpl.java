package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.AuthorityDAO;
import com.aimacademyla.model.Authority;
import com.aimacademyla.model.enums.AIMEntityType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("authorityDAO")
@Transactional
public class AuthorityDAOImpl extends GenericDAOImpl<Authority, String> implements AuthorityDAO{

    private final AIMEntityType AIM_ENTITY_TYPE = AIMEntityType.AUTHORITY;

    public AuthorityDAOImpl() {
        super(Authority.class);
    }

    @Override
    public AIMEntityType getAIMEntityType() {
        return AIM_ENTITY_TYPE;
    }
}
