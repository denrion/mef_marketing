package com.github.denrion.mef_marketing.entity;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity(name = "Mail")
@Table(name = "mail")
public class Mail extends AbstractEntityWithoutId {

    @Id
    private Long id;

    @Basic
    @Column(name = "date_mail_received")
    @JsonbDateFormat("yyyy-mm-dd")
    private LocalDate dateMailReceived;

    @Basic
    @Column(name = "date_mail_received_on_upis")
    @JsonbDateFormat("yyyy-mm-dd")
    private LocalDate getDateMailReceivedOnUpis;

    @Basic
    @Column(name = "date_replay")
    @JsonbDateFormat("yyyy-mm-dd")
    private LocalDate dateReplay;

    @Basic
    @Column(name = "email_which_received")
    private String emailWhichReceived;

    @Basic
    @Column(name = "price",
            precision = 6, scale = 2)
    private BigDecimal price;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "potential_student_id", nullable = false,
            foreignKey = @ForeignKey(name = "potential_student_mail_fk"))
    @MapsId
    private PotentialStudent potentialStudent;


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
}
