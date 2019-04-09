package com.github.denrion.mef_marketing.entity;

import com.github.denrion.mef_marketing.config.LocalDateAdapter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

import static com.github.denrion.mef_marketing.entity.PotentialStudentPopup.GET_ALL_POTENTIAL_STUDENTS_POPUP;
import static com.github.denrion.mef_marketing.entity.PotentialStudentPopup.GET_POTENTIAL_STUDENT_POPUP_BY_ID;

@Entity(name = "PotentialStudentPopup")
@Table(name = "popup")
@DynamicUpdate // HIBERNATE ONLY!!!!!!!!!!!
@NamedQuery(name = GET_ALL_POTENTIAL_STUDENTS_POPUP,
        query = "SELECT popup FROM PotentialStudentPopup popup JOIN FETCH popup.potentialStudent")
@NamedQuery(name = GET_POTENTIAL_STUDENT_POPUP_BY_ID,
        query = "SELECT popup FROM PotentialStudentPopup popup JOIN FETCH popup.potentialStudent WHERE :id = popup.potentialStudent.id")
public class PotentialStudentPopup extends AbstractEntityWithoutId {

    // NAMED QUERIES

    public static final String GET_ALL_POTENTIAL_STUDENTS_POPUP = "PotentialStudentPopup.getAll";
    public static final String GET_POTENTIAL_STUDENT_POPUP_BY_ID = "PotentialStudentPopup.getById";


    // FIELDS

    @Id
    private Long id;

    @Basic
    @Column(name = "date_contact")
    @FormParam("dateContact")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate dateContact;

    @Basic
    @Column(name = "date_sign_up")
    @FormParam("dateSignUp")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate dateSignUp;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "potential_student_id", nullable = false, insertable = false,
            foreignKey = @ForeignKey(name = "fk_potential_student_eform"))
    @MapsId
    private PotentialStudent potentialStudent;


    // GETTERS AND SETTERS


    public Long getId() {
        return id;
    }

    public LocalDate getDateContact() {
        return dateContact;
    }

    public void setDateContact(LocalDate dateContact) {
        this.dateContact = dateContact;
    }

    public LocalDate getDateSignUp() {
        return dateSignUp;
    }

    public void setDateSignUp(LocalDate dateSignUp) {
        this.dateSignUp = dateSignUp;
    }

    public PotentialStudent getPotentialStudent() {
        return potentialStudent;
    }

    public void setPotentialStudent(PotentialStudent potentialStudent) {
        this.potentialStudent = potentialStudent;
    }
}
