package com.github.denrion.mef_marketing.entity;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;

import static com.github.denrion.mef_marketing.entity.PotentialStudent.GET_ALL_POTENTIAL_STUDENTS;
import static com.github.denrion.mef_marketing.entity.PotentialStudent.GET_POTENTIAL_STUDENT_BY_EMAIL;

@Entity(name = "PotentialStudent")
@Table(name = "potential_student", uniqueConstraints =
@UniqueConstraint(name = "email_unique", columnNames = {"email"}))
@DynamicUpdate // HIBERNATE ONLY!!!!!!!!!!!
@NamedQuery(name = GET_ALL_POTENTIAL_STUDENTS,
        query = "SELECT ps FROM PotentialStudent ps")
@NamedQuery(name = GET_POTENTIAL_STUDENT_BY_EMAIL,
        query = "SELECT ps FROM PotentialStudent ps WHERE ps.email = :email")
public class PotentialStudent extends AbstractEntityWithId {

    // NAMED QUERIES

    public static final String GET_ALL_POTENTIAL_STUDENTS = "PotentialStudent.getAll";
    public static final String GET_POTENTIAL_STUDENT_BY_EMAIL = "PotentialStudent.getByEmail";


    // FIELDS

    @Basic
    @Column(name = "email")
    @Email(message = "It must be a valid email")
    @FormParam("email")
    private String email;

    @Basic
    @Column(name = "full_name", nullable = false, length = 80)
    @NotBlank(message = "not blank")
    @Size(max = 80, message = "Max 80 characters")
    @FormParam("fullName")
    private String fullName;

    @Basic
    @Column(name = "phone")
    @FormParam("phone")
    private String phone;

//    @OneToMany(mappedBy = "potentialStudent", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<EntranceTest> tests = new ArrayList<>();
//
//    public void addEntranceTest(EntranceTest test) {
//        tests.add(test);
//        test.setPotentialStudent(this);
//    }
//
//    public void removeEntranceTest(EntranceTest test) {
//        tests.remove(test);
//        test.setPotentialStudent(null);
//    }


    // GETTERS AND SETTERS

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "PotentialStudent{" +
                "email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
