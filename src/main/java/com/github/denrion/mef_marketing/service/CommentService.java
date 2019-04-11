package com.github.denrion.mef_marketing.service;

import com.github.denrion.mef_marketing.entity.Comment;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

import static com.github.denrion.mef_marketing.entity.Comment.*;

@Stateless
@LocalBean
public class CommentService implements GenericService<Comment> {

    @Inject
    EntityManager entityManager;

    @Override
    public Optional<Comment> getById(Long id) {
        List<Comment> list = entityManager
                .createNamedQuery(GET_COMMENT_BY_ID, Comment.class)
                .setParameter("id", id)
                .getResultList();

        return list.isEmpty() ? Optional.empty() : Optional.ofNullable(list.get(0));
    }

    @Override
    public List<Comment> getAll() {
        return entityManager
                .createNamedQuery(GET_ALL_COMMENTS, Comment.class)
                .getResultList();
    }

    public List<Comment> getAllByPSId(Long ps_id) {
        return entityManager
                .createNamedQuery(GET_COMMENTS_BY_POTENTIAL_STUDENT_ID,
                        Comment.class)
                .setParameter("ps_id", ps_id)
                .getResultList();
    }

    @Override
    public Comment save(Comment comment) {
        entityManager.persist(comment);
        return comment;
    }

    @Override
    public Optional<Comment> update(Comment newComment, Long id) {
        Comment oldComment = getById(id)
                .orElseThrow(NotFoundException::new);

        updateCommentFields(oldComment, newComment);

        return Optional.ofNullable(entityManager.merge(oldComment));
    }

    @Override
    public void delete(Long id) {
        Comment comment = getById(id)
                .orElseThrow(NotFoundException::new);

        entityManager.remove(comment);
    }

    private void updateCommentFields(Comment oldComment, Comment newComment) {
        oldComment.setComment(newComment.getComment());
    }

}
