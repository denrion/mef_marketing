package com.github.denrion.mef_marketing.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity(name = "AdminUser")
@Table(name = "admin_user", uniqueConstraints =
@UniqueConstraint(name = "username_unique", columnNames = {"username"}))
@NamedQuery(name = AdminUser.GET_ALL_USERS,
        query = "SELECT u FROM AdminUser u")
public class AdminUser extends AbstractEntity {

    public static final String GET_ALL_USERS = "AdminUser.getAll";

    @Basic
    @Column(name = "full_name", length = 80)
    @Size(max = 80, message = "max 80")
    private String fullName;

    @Basic
    @Column(name = "password")
    @NotBlank(message = "Not Blank")
    private String password;

    @Basic
    @Column(name = "username")
    @NotBlank(message = "Not Blank")
    private String username;


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
