package com.aimacademyla.model.id.generator;

import com.aimacademyla.model.Member;

import java.util.List;

public class MemberIDGenerator extends DefaultIDGenerator{

    @Override
    public int generateID(Class classType) {
        int id = super.generateID(classType);
        return (id <= 1000) ? 1001 : id;
    }
}
