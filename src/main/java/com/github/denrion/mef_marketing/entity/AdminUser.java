package com.github.denrion.mef_marketing.entity;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;

@Entity(name = "AdminUser")
@Table(name = "admin_user", uniqueConstraints =
@UniqueConstraint(name = "username_unique", columnNames = {"username"}))
@DynamicUpdate // HIBERNATE ONLY!!!!!!!!!!!
@NamedQuery(name = AdminUser.GET_ALL_USERS,
        query = "SELECT u FROM AdminUser u")
@NamedQuery(name = AdminUser.GET_USER_BY_USERNAME,
        query = "SELECT u FROM AdminUser u WHERE u.username = :username")
public class AdminUser extends AbstractEntityWithId {

    // NAMED QUERIES

    public static final String GET_ALL_USERS = "AdminUser.getAll";
    public static final String GET_USER_BY_USERNAME = "AdminUser.getByUsername";

    // FIELDS

    @Basic
    @Column(name = "full_name", length = 80)
    @Size(max = 80, message = "max 80")
    @FormParam("fullName")
    private String fullName;

    @Basic
    @Column(name = "password")
    @NotBlank(message = "Not Blank")
    @FormParam("password")
    private String password;

    @Basic
    @Column(name = "username")
    @NotBlank(message = "Not Blank")
    @FormParam("username")
    private String username;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<EntranceTest> tests = new ArrayList<>();
//
//    public void addEntranceTest(EntranceTest test) {
//        tests.add(test);
//        test.setUser(this);
//    }
//
//    public void removeEntranceTest(EntranceTest test) {
//        tests.remove(test);
//        test.setUser(null);
//    }


    // GETTERS AND SETTERS

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

    @Override
    public String toString() {
        return "AdminUser{" +
                "fullName='" + fullName + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
