package com.aimacademyla.model.id.generator;

import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.AIMEntity;
import com.aimacademyla.util.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;

import java.util.List;

public class DefaultIDGenerator{

    private DAOFactory daoFactory;

    public DefaultIDGenerator(){
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        daoFactory = (DAOFactory)context.getBean("daoFactory");
    }

    public int generateID(Class classType){
        return fetchID(classType) + 1;
    }
    /*
     * Fetch the largest business ID number for an entity
     */
    @SuppressWarnings("unchecked")
    private int fetchID(Class classType){
        List<AIMEntity> aimEntityList = daoFactory.getDAO(classType).getList();
        return getLargestID(aimEntityList);
    }

    private int getLargestID(List<AIMEntity> aimEntityList){
        int largestID = 0;
        for(AIMEntity aimEntity : aimEntityList)
            if(aimEntity.getBusinessID() > largestID)
                largestID = aimEntity.getBusinessID();

        return largestID;
    }
}
