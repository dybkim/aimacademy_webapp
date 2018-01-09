package com.aimacademyla.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.stereotype.Component;

@Component
public class HibernateAwareObjectMapper extends ObjectMapper{
    public HibernateAwareObjectMapper(){
        registerModule(new Hibernate5Module());
    }
}
