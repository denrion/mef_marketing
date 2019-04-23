package com.github.denrion.mef_marketing.entity;

import org.hibernate.annotations.DynamicUpdate;

import javax.json.JsonObjectBuilder;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.math.BigDecimal;
import java.time.LocalDate;

import static com.github.denrion.mef_marketing.entity.PotentialStudentMail.GET_ALL_POTENTIAL_STUDENTS_MAIL;
import static com.github.denrion.mef_marketing.entity.PotentialStudentMail.GET_POTENTIAL_STUDENT_MAIL_BY_ID;

@Entity(name = "PotentialStudentMail")
@Table(name = "mail")
@DynamicUpdate // HIBERNATE ONLY!!!!!!!!!!!
@NamedQuery(name = GET_ALL_POTENTIAL_STUDENTS_MAIL,
        query = "SELECT psm FROM PotentialStudentMail psm JOIN FETCH psm.potentialStudent")
@NamedQuery(name = GET_POTENTIAL_STUDENT_MAIL_BY_ID,
        query = "SELECT psm FROM PotentialStudentMail psm JOIN FETCH psm.potentialStudent WHERE :id = psm.potentialStudent.id")
public class PotentialStudentMail extends AbstractEntityWithoutId {

    // NAMED QUERIES

    public static final String GET_ALL_POTENTIAL_STUDENTS_MAIL = "PotentialStudentMail.getAll";
    public static final String GET_POTENTIAL_STUDENT_MAIL_BY_ID = "PotentialStudentMail.getById";


    // FIELDS

    @Id
    private Long id;

    @Basic
    @Column(name = "date_mail_received")
    private LocalDate dateMailReceived;

    @Basic
    @Column(name = "date_mail_received_on_upis")
    private LocalDate dateMailReceivedOnUpis;

    @Basic
    @Column(name = "date_reply")
    private LocalDate dateReply;

    @Basic
    @Column(name = "email_which_received")
    @Email(message = "It must be a valid email")
    private String emailWhichReceived;

    @Basic
    @Column(name = "price", precision = 6, scale = 2)
    private BigDecimal price;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "potential_student_id", nullable = false, insertable = false,
            foreignKey = @ForeignKey(name = "fk_potential_student_mail"))
    @MapsId
    @Valid
    private PotentialStudent potentialStudent;

    @Override
    public JsonObjectBuilder toJson() {
        return null;
    }


    // GETTERS AND SETTERS

    public Long getId() {
        return id;
    }

    public LocalDate getDateMailReceived() {
        return dateMailReceived;
    }

    public void setDateMailReceived(LocalDate dateMailReceived) {
        this.dateMailReceived = dateMailReceived;
    }

    public LocalDate getDateMailReceivedOnUpis() {
        return dateMailReceivedOnUpis;
    }

    public void setDateMailReceivedOnUpis(LocalDate dateMailReceivedOnUpis) {
        this.dateMailReceivedOnUpis = dateMailReceivedOnUpis;
    }

    public LocalDate getDateReply() {
        return dateReply;
    }

    public void setDateReply(LocalDate dateReplay) {
        this.dateReply = dateReplay;
    }

    public String getEmailWhichReceived() {
        return emailWhichReceived;
    }

    public void setEmailWhichReceived(String emailWhichReceived) {
        this.emailWhichReceived = emailWhichReceived;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public PotentialStudent getPotentialStudent() {
        return potentialStudent;
    }

    public void setPotentialStudent(PotentialStudent potentialStudent) {
        this.potentialStudent = potentialStudent;
    }

    @Override
    public String toString() {
        return "PotentialStudentMail{" +
                "potentialStudent=" + potentialStudent +
                ", dateMailReceived=" + dateMailReceived +
                ", dateMailReceivedOnUpis=" + dateMailReceivedOnUpis +
                ", dateReplay=" + dateReply +
                ", emailWhichReceived='" + emailWhichReceived + '\'' +
                ", price=" + price +
                '}';
    }
}
