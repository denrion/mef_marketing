package com.github.denrion.mef_marketing.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity(name = "PotentialStudent")
@Table(name = "potential_student", uniqueConstraints =
@UniqueConstraint(name = "email_unique", columnNames = {"email"}))
public class PotentialStudent extends AbstractEntityWithId {

    @Basic
    @Column(name = "email")
    @Email(message = "It must be a valid email")
    private String email;

    @Basic
    @Column(name = "full_name", nullable = false, length = 80)
    @NotBlank(message = "not blank")
    @Size(max = 80, message = "Max 80 characters")
    private String fullName;

    @Basic
    @Column(name = "phone")
    private String phone;
}
