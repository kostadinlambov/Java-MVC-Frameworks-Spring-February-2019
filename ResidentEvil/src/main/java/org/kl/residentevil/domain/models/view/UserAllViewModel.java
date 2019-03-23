package org.kl.residentevil.domain.models.view;

import org.kl.residentevil.domain.entities.UserRole;

public class UserAllViewModel {
    private String id;
    private String username;
    private String email;
    private String role;

    public UserAllViewModel() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
