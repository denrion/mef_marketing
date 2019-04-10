package com.github.denrion.mef_marketing.service;

import com.github.denrion.mef_marketing.entity.Meeting;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

@Stateless
@LocalBean
public class MeetingService implements GenericService<Meeting> {

    @Inject
    EntityManager entityManager;

    @Inject
    PotentialStudentService studentService;

    @Override
    public Optional<Meeting> getById(Long id) {
        List<Meeting> list = entityManager
                .createNamedQuery(Meeting.GET_MEETING_BY_ID,
                        Meeting.class)
                .setParameter("id", id)
                .getResultList();

        return list.isEmpty() ? Optional.empty() : Optional.ofNullable(list.get(0));
    }

    @Override
    public List<Meeting> getAll() {
        return entityManager
                .createNamedQuery(Meeting.GET_ALL_MEETINGS,
                        Meeting.class)
                .getResultList();
    }

    @Override
    public Meeting save(Meeting meeting) {
        // TODO check if meeting already exists in the DB
        entityManager.persist(meeting);

        return meeting;
    }

    @Override
    public Optional<Meeting> update(Meeting newMeeting, Long id) {
        Meeting oldMeeting = getById(id)
                .orElseThrow(NotFoundException::new);

        studentService.updatePSFields(oldMeeting.getPotentialStudent(), newMeeting.getPotentialStudent());
        updateMeetingFields(oldMeeting, newMeeting);

        return Optional.ofNullable(entityManager.merge(oldMeeting));
    }

    @Override
    public void delete(Long id) {
        Meeting meeting = getById(id)
                .orElseThrow(NotFoundException::new);

        entityManager.remove(meeting);
    }

    private void updateMeetingFields(Meeting oldMeeting, Meeting newMeeting) {
        oldMeeting.setComment(newMeeting.getComment());
        oldMeeting.setDateDeal(newMeeting.getDateDeal());
        oldMeeting.setVisitTime(newMeeting.getVisitTime());
        oldMeeting.setWhoInvited(newMeeting.getWhoInvited());
    }

}
