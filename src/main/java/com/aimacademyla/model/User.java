package com.aimacademyla.model;

import com.aimacademyla.model.enums.AIMEntityType;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
public class User implements Serializable{

    private static final long serialVersionUID = 8804121618393206327L;

    @Id
    @Column(name="username")
    @NotNull
    @Length(max = 30)
    private String username;

    @Column(name="password")
    @Length(max = 30)
    private String password;

    @Column(name="enabled")
    private boolean isEnabled;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
