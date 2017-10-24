package com.aimacademyla.model.enums;

public enum BillableUnitType{
    PER_HOUR("hour"),
    PER_SESSION("session"),
    ERROR("error");

    private String type;

    BillableUnitType(String type){
        this.type = type;
    }

    public String toString(){
        return type;
    }

    public static BillableUnitType parseString(String type){
        switch(type){
            case "hour":
                return PER_HOUR;
            case "session":
                return PER_SESSION;
            default:
                return ERROR;
        }
    }
}
