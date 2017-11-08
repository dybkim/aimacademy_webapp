package com.aimacademyla.model.wrapper;

import java.io.Serializable;

public class NewPasswordFormWrapper implements Serializable{

    private static final long serialVersionUID = 7657257718520268689L;

    private String currentPassword;

    private String newPassword;

    private String confirmPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
