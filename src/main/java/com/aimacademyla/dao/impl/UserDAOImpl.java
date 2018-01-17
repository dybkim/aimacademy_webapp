package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.UserDAO;
import com.aimacademyla.model.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository("userDAO")
@Transactional
public class UserDAOImpl extends GenericDAOImpl<User, String> implements UserDAO{
    public UserDAOImpl(){
        super(User.class);
    }

    @Override
    public void removeList(List<User> userList){
        Session session = currentSession();
        List<String> userIDList = new ArrayList<>();
        for(User user : userList)
            userIDList.add(user.getUsername());
        Query query = session.createQuery("DELETE FROM User U WHERE U.username in :userIDList");
        query.setParameterList("userIDList", userIDList);
        query.executeUpdate();
    }
}
