package com.github.denrion.mef_marketing.entity;

import javax.json.JsonObjectBuilder;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

import static com.github.denrion.mef_marketing.entity.PotentialStudentPhone.GET_ALL_POTENTIAL_STUDENTS_PHONE;
import static com.github.denrion.mef_marketing.entity.PotentialStudentPhone.GET_POTENTIAL_STUDENT_PHONE_BY_ID;

@Entity(name = "PotentialStudentPhone")
@Table(name = "phone")
@NamedQuery(name = GET_ALL_POTENTIAL_STUDENTS_PHONE,
        query = "SELECT psp FROM PotentialStudentPhone psp JOIN FETCH psp.potentialStudent")
@NamedQuery(name = GET_POTENTIAL_STUDENT_PHONE_BY_ID,
        query = "SELECT psp FROM PotentialStudentPhone psp JOIN FETCH psp.potentialStudent WHERE :id = psp.potentialStudent.id")

public class PotentialStudentPhone extends AbstractEntityWithoutId {

    // NAMED QUERIES

    public static final String GET_ALL_POTENTIAL_STUDENTS_PHONE = "PotentialStudentPhone.getAll";
    public static final String GET_POTENTIAL_STUDENT_PHONE_BY_ID = "PotentialStudentPhone.getById";


    // FIELDS

    @Id
    private Long id;

    @Basic
    @Column(name = "city", length = 100)
    @Size(max = 100)
    private String city;

    @Basic
    @Column(name = "heard_of", columnDefinition = "TEXT")
    private String heardOf;

    @Basic
    @Column(name = "study_year")
    private int studyYear;

    @Basic
    @Column(name = "date_visit")
    private LocalDate dateVisit;

    @Basic
    @Column(name = "price",
            precision = 6, scale = 2)
    private BigDecimal price;

    @Basic
    @Column(name = "who_called_mef")
    private String whoCalledMef;

    @Basic
    @Column(name = "enrollment", columnDefinition = "TEXT")
    private String enrollment;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "potential_student_id", nullable = false, insertable = false,
            foreignKey = @ForeignKey(name = "fk_potential_student_phone"))
    @MapsId
    private PotentialStudent potentialStudent;

    @Override
    public JsonObjectBuilder toJson() {
        return null;
    }


    // GETTERS AND SETTERS

    public Long getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHeardOf() {
        return heardOf;
    }

    public void setHeardOf(String hearOf) {
        this.heardOf = hearOf;
    }

    public int getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(int studyYear) {
        this.studyYear = studyYear;
    }

    public LocalDate getDateVisit() {
        return dateVisit;
    }

    public void setDateVisit(LocalDate dateVisit) {
        this.dateVisit = dateVisit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getWhoCalledMef() {
        return whoCalledMef;
    }

    public void setWhoCalledMef(String whoCalledMef) {
        this.whoCalledMef = whoCalledMef;
    }

    public String getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(String enrollment) {
        this.enrollment = enrollment;
    }

    public PotentialStudent getPotentialStudent() {
        return potentialStudent;
    }

    public void setPotentialStudent(PotentialStudent potentialStudent) {
        this.potentialStudent = potentialStudent;
    }

    @Override
    public String toString() {
        return "PotentialStudentPhone{" +
                "potentialStudent=" + potentialStudent +
                ", city='" + city + '\'' +
                ", hearOf='" + heardOf + '\'' +
                ", studyYear=" + studyYear +
                ", dateVisit=" + dateVisit +
                ", price=" + price +
                ", whoCalledMef='" + whoCalledMef + '\'' +
                ", enrollment='" + enrollment + '\'' +
                '}';
    }
}
