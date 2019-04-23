package com.github.denrion.mef_marketing.entity;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.DynamicUpdate;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.github.denrion.mef_marketing.entity.AppUser.GET_ALL_USERS;
import static com.github.denrion.mef_marketing.entity.AppUser.GET_USER_BY_USERNAME;

@Schema(name = "AppUser", description = "POJO that represents an AppUser")
@Entity(name = "AppUser")
@Table(name = "app_user", uniqueConstraints =
@UniqueConstraint(name = "username_unique", columnNames = {"username"}))
@DynamicUpdate // HIBERNATE ONLY!!!!!!!!!!!
@NamedQuery(name = GET_ALL_USERS,
        query = "SELECT u FROM AppUser u ORDER BY u.createdAt")
@NamedQuery(name = GET_USER_BY_USERNAME,
        query = "SELECT u FROM AppUser u WHERE u.username = :username")
public class AppUser extends AbstractEntityWithId {

    // NAMED QUERIES

    public static final String GET_ALL_USERS = "AppUser.getAll";
    public static final String GET_USER_BY_USERNAME = "AppUser.getByUsername";

    // FIELDS

    @Schema(example = "name")
    @Basic
    @Column(name = "full_name", length = 80)
    @Size(max = 80, message = "max 80")
    private String fullName;

    @Schema(required = true, example = "username")
    @Basic
    @Column(name = "username")
    @NotBlank(message = "Not Blank")
    private String username;

    @Schema(required = true, example = "password")
    @Basic
    @Column(name = "password")
    @NotBlank(message = "Not Blank")
//    @StrongPassword
    private String password;

    @Schema(required = true, example = "admin")
    @Basic
    @Column(name = "role")
    @NotBlank(message = "Not Blank")
    @Pattern(regexp = "admin|user")
    private String role;

    @Basic
    @Column(name = "salt")
    private String salt;

    // JSON REPRESENTATION

    @Override
    public JsonObjectBuilder toJson() {
        return Json.createObjectBuilder()
                .add("username", getUsername())
                .add("fullName", getFullName());
    }

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }


}
