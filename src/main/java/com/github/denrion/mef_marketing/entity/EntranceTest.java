package com.github.denrion.mef_marketing.entity;

import com.github.denrion.mef_marketing.config.LocalDateAdapter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.Objects;

@Entity(name = "EntranceTest")
@Table(name = "entrance_test")
@DynamicUpdate // HIBERNATE ONLY!!!!!!!!!!!
@NamedQuery(name = EntranceTest.GET_ALL_ENTRANCE_TESTS,
        query = "SELECT test FROM EntranceTest test JOIN FETCH test.potentialStudent JOIN FETCH test.user")
@NamedQuery(name = EntranceTest.GET_ENTRANCE_TEST_BY_ID,
        query = "SELECT test FROM EntranceTest test JOIN FETCH test.potentialStudent JOIN FETCH test.user WHERE test.id = :id")
public class EntranceTest extends AbstractEntityWithId {

    // NAMED QUERIES

    public static final String GET_ALL_ENTRANCE_TESTS = "EntranceTest.getAll";
    public static final String GET_ENTRANCE_TEST_BY_ID = "EntranceTest.getById";


    // FIELDS

    @Basic
    @Column(name = "date_deal")
    @FormParam("dateDeal")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate dateDeal;

    @Basic
    @Column(name = "date_mail_sent")
    @FormParam("dateMailSent")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate dateMailSent;

    @Basic
    @Column(name = "mail_content", columnDefinition = "TEXT")
    @FormParam("mailContent")
    private String mailContent;

    @Basic
    @Column(name = "comment", columnDefinition = "TEXT")
    @FormParam("comment")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "potential_student_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_potential_student_entrance_test"))
    private PotentialStudent potentialStudent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_entrance_test"))
    private AdminUser user;


    // EQUALS AND HASH CODE

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntranceTest that = (EntranceTest) o;
        return Objects.equals(getDateDeal(), that.getDateDeal()) &&
                Objects.equals(getDateMailSent(), that.getDateMailSent()) &&
                Objects.equals(getMailContent(), that.getMailContent()) &&
                Objects.equals(getComment(), that.getComment()) &&
                Objects.equals(getPotentialStudent(), that.getPotentialStudent()) &&
                Objects.equals(getUser(), that.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDateDeal(), getDateMailSent(), getMailContent(), getComment(), getPotentialStudent(), getUser());
    }

    // GETTERS AND SETTERS

    public LocalDate getDateDeal() {
        return dateDeal;
    }

    public void setDateDeal(LocalDate dateDeal) {
        this.dateDeal = dateDeal;
    }

    public LocalDate getDateMailSent() {
        return dateMailSent;
    }

    public void setDateMailSent(LocalDate dateMailSent) {
        this.dateMailSent = dateMailSent;
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public PotentialStudent getPotentialStudent() {
        return potentialStudent;
    }

    public void setPotentialStudent(PotentialStudent potentialStudent) {
        this.potentialStudent = potentialStudent;
    }

    public AdminUser getUser() {
        return user;
    }

    public void setUser(AdminUser user) {
        this.user = user;
    }


    @Override
    public String toString() {
        return "EntranceTest{" +
                "dateDeal=" + dateDeal +
                ", dateMailSent=" + dateMailSent +
                ", mailContent='" + mailContent + '\'' +
                ", comment='" + comment + '\'' +
                ", potentialStudent=" + potentialStudent +
                ", user=" + user +
                '}';
    }
}
