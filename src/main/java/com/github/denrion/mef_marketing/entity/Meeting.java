package com.github.denrion.mef_marketing.entity;

import org.hibernate.annotations.DynamicUpdate;

import javax.json.JsonObjectBuilder;
import javax.persistence.*;
import java.time.LocalDate;

import static com.github.denrion.mef_marketing.entity.Meeting.GET_ALL_MEETINGS;
import static com.github.denrion.mef_marketing.entity.Meeting.GET_MEETING_BY_ID;

@Entity(name = "Meeting")
@Table(name = "meeting")
@DynamicUpdate // HIBERNATE ONLY!!!!!!!!!!!
@NamedQuery(name = GET_ALL_MEETINGS,
        query = "SELECT meeting FROM Meeting meeting JOIN FETCH meeting.potentialStudent")
@NamedQuery(name = GET_MEETING_BY_ID,
        query = "SELECT meeting FROM Meeting meeting JOIN FETCH meeting.potentialStudent WHERE meeting.id = :id")
public class Meeting extends AbstractEntityWithoutId {

    // NAMED QUERIES

    public static final String GET_ALL_MEETINGS = "Meeting.getAll";
    public static final String GET_MEETING_BY_ID = "Meeting.getById";


    // FIELDS

    @Id
    private Long id;

    @Basic
    @Column(name = "date_deal")
    private LocalDate dateDeal;

    @Basic
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Basic
    @Column(name = "visit_time", columnDefinition = "TEXT")
    private String visitTime;

    @Basic
    @Column(name = "who_invited")
    private String whoInvited;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "potential_student_id", nullable = false, insertable = false,
            foreignKey = @ForeignKey(name = "fk_potential_student_meeting"))
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

    public LocalDate getDateDeal() {
        return dateDeal;
    }

    public void setDateDeal(LocalDate dateDeal) {
        this.dateDeal = dateDeal;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }

    public String getWhoInvited() {
        return whoInvited;
    }

    public void setWhoInvited(String whoInvited) {
        this.whoInvited = whoInvited;
    }

    public PotentialStudent getPotentialStudent() {
        return potentialStudent;
    }

    public void setPotentialStudent(PotentialStudent potentialStudent) {
        this.potentialStudent = potentialStudent;
    }
}
