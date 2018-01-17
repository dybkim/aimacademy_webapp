package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.AuthorityDAO;
import com.aimacademyla.model.Authority;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository("authorityDAO")
@Transactional
public class AuthorityDAOImpl extends GenericDAOImpl<Authority, String> implements AuthorityDAO{

    public AuthorityDAOImpl() {
        super(Authority.class);
    }

    @Override
    public void removeList(List<Authority> authorityList){
        Session session = currentSession();
        List<String> authorityIDList = new ArrayList<>();
        for(Authority authority : authorityList)
            authorityIDList.add(authority.getUsername());
        Query query = session.createQuery("DELETE FROM Authority A WHERE A.username in :authorityIDList");
        query.setParameterList("authorityIDList", authorityIDList);
        query.executeUpdate();
    }
}
