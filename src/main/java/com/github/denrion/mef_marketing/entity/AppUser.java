package com.github.denrion.mef_marketing.entity;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.github.denrion.mef_marketing.entity.AppUser.GET_ALL_USERS;
import static com.github.denrion.mef_marketing.entity.AppUser.GET_USER_BY_USERNAME;

@Entity(name = "AppUser")
@Table(name = "app_user", uniqueConstraints =
@UniqueConstraint(name = "username_unique", columnNames = {"username"}))
@DynamicUpdate // HIBERNATE ONLY!!!!!!!!!!!
@NamedQuery(name = GET_ALL_USERS,
        query = "SELECT u FROM AppUser u")
@NamedQuery(name = GET_USER_BY_USERNAME,
        query = "SELECT u FROM AppUser u WHERE u.username = :username")
public class AppUser extends AbstractEntityWithId {

    // NAMED QUERIES

    public static final String GET_ALL_USERS = "AppUser.getAll";
    public static final String GET_USER_BY_USERNAME = "AppUser.getByUsername";

    // FIELDS

    @Basic
    @Column(name = "full_name", length = 80)
    @Size(max = 80, message = "max 80")
    private String fullName;

    @Basic
    @Column(name = "username")
    @NotBlank(message = "Not Blank")
    private String username;

    @Basic
    @Column(name = "password")
    @NotBlank(message = "Not Blank")
    private String password;

    @Basic
    @Column(name = "salt")
    private String salt;


    // GETTERS AND SETTERS

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "fullName='" + fullName + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
