package com.aimacademyla.model.id;

import com.aimacademyla.model.Member;
import com.aimacademyla.model.id.generator.DefaultIDGenerator;
import com.aimacademyla.model.id.generator.MemberIDGenerator;

public class IDGenerationStrategy{

    public static int generateID(Class classType){

        //Too lazy right now to come up with some clever class->enum conversion strategy just to make a switch statement
        if(classType == Member.class){
            return new MemberIDGenerator().generateID(classType);
        }

        else
            return new DefaultIDGenerator().generateID(classType);
    }
}
