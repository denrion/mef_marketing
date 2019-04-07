package com.github.denrion.mef_marketing.entity;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import static com.github.denrion.mef_marketing.entity.PotentialStudentMail.GET_ALL_POTENTIAL_STUDENTS_BY_MAIL;
import static com.github.denrion.mef_marketing.entity.PotentialStudentMail.GET_POTENTIAL_STUDENT_MAIL_BY_ID;

@Entity(name = "PSByMail")
@Table(name = "mail")
@NamedQuery(name = GET_ALL_POTENTIAL_STUDENTS_BY_MAIL,
        query = "SELECT DISTINCT psm FROM PSByMail psm JOIN FETCH psm.potentialStudent ps")
@NamedQuery(name = GET_POTENTIAL_STUDENT_MAIL_BY_ID,
        query = "SELECT DISTINCT psm FROM PSByMail psm JOIN FETCH psm.potentialStudent ps WHERE ps.id = :id")
@DynamicUpdate
public class PotentialStudentMail extends AbstractEntityWithoutId {

    public static final String GET_ALL_POTENTIAL_STUDENTS_BY_MAIL = "PotentialStudentMail.getAll";
    public static final String GET_POTENTIAL_STUDENT_MAIL_BY_ID = "PotentialStudentsMail.getById";

    @Id
    private Long id;

    @Basic
    @Column(name = "date_mail_received")
    private LocalDate dateMailReceived;

    @Basic
    @Column(name = "date_mail_received_on_upis")
    private LocalDate getDateMailReceivedOnUpis;

    @Basic
    @Column(name = "date_replay")
    private LocalDate dateReplay;

    @Basic
    @Column(name = "email_which_received")
    private String emailWhichReceived;

    @Basic
    @Column(name = "price",
            precision = 6, scale = 2)
    private BigDecimal price;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "potential_student_id", nullable = false, insertable = false,
            foreignKey = @ForeignKey(name = "fk_potential_student_mail"))
    @MapsId
    private PotentialStudent potentialStudent;


    public Long getId() {
        return id;
    }

    public LocalDate getDateMailReceived() {
        return dateMailReceived;
    }

    public void setDateMailReceived(LocalDate dateMailReceived) {
        this.dateMailReceived = dateMailReceived;
    }

    public LocalDate getGetDateMailReceivedOnUpis() {
        return getDateMailReceivedOnUpis;
    }

    public void setGetDateMailReceivedOnUpis(LocalDate getDateMailReceivedOnUpis) {
        this.getDateMailReceivedOnUpis = getDateMailReceivedOnUpis;
    }

    public LocalDate getDateReplay() {
        return dateReplay;
    }

    public void setDateReplay(LocalDate dateReplay) {
        this.dateReplay = dateReplay;
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
                ", dateMailReceived=" + dateMailReceived +
                ", getDateMailReceivedOnUpis=" + getDateMailReceivedOnUpis +
                ", dateReplay=" + dateReplay +
                ", emailWhichReceived='" + emailWhichReceived + '\'' +
                ", price=" + price;
    }
}
