package com.github.denrion.mef_marketing.entity;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Objects;

import static com.github.denrion.mef_marketing.entity.Comment.*;

@Entity(name = "Comment")
@Table(name = "comment")
@DynamicUpdate // HIBERNATE ONLY!!!!!!!!!!!
@NamedQuery(name = GET_ALL_COMMENTS,
        query = "SELECT comm FROM Comment comm JOIN FETCH comm.potentialStudent JOIN FETCH comm.user")
@NamedQuery(name = GET_COMMENT_BY_ID,
        query = "SELECT comm FROM Comment comm JOIN FETCH comm.potentialStudent JOIN FETCH comm.user WHERE comm.id = :id")
@NamedQuery(name = GET_COMMENTS_BY_POTENTIAL_STUDENT_ID,
        query = "SELECT comm FROM Comment comm WHERE comm.potentialStudent.id = :ps_id")
public class Comment extends AbstractEntityWithId {

    // NAMED QUERIES

    public static final String GET_ALL_COMMENTS = "Comment.getAll";
    public static final String GET_COMMENT_BY_ID = "Comment.getById";
    public static final String GET_COMMENTS_BY_POTENTIAL_STUDENT_ID = "Comment.getByPSId";


    // FIELDS

    @Basic
    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "potential_student_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_potential_student_comment"))
    private PotentialStudent potentialStudent;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_comment"))
    private AppUser user;

    // GETTERS AND SETTERS

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

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }


    // EQUALS AND HASH CODE

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment1 = (Comment) o;
        return Objects.equals(getComment(), comment1.getComment()) &&
                Objects.equals(getPotentialStudent(), comment1.getPotentialStudent()) &&
                Objects.equals(getUser(), comment1.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getComment(), getPotentialStudent(), getUser());
    }
}
