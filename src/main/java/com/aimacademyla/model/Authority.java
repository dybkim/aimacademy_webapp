package com.aimacademyla.model;

import com.aimacademyla.model.enums.AIMEntityType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
public class Authority implements Serializable{

    private static final long serialVersionUID = 7016548446103778363L;

    @Id
    @Column(name="username")
    @NotNull
    private String username;

    @Column(name="authority")
    private String authority;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
