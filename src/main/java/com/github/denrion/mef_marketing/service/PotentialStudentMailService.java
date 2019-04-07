package com.github.denrion.mef_marketing.service;

import com.github.denrion.mef_marketing.entity.PotentialStudent;
import com.github.denrion.mef_marketing.entity.PotentialStudentMail;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.github.denrion.mef_marketing.entity.PotentialStudentMail.GET_ALL_POTENTIAL_STUDENTS_BY_MAIL;
import static com.github.denrion.mef_marketing.entity.PotentialStudentMail.GET_POTENTIAL_STUDENT_MAIL_BY_ID;

@Stateless
@LocalBean
public class PotentialStudentMailService implements GenericService<PotentialStudentMail> {

    @Inject
    EntityManager entityManager;

    @Inject
    PotentialStudentService studentService;

    @Override
    public Optional<PotentialStudentMail> getById(Long id) {
        return Optional.ofNullable(entityManager.find(PotentialStudentMail.class, id));
    }

    public Optional<PotentialStudentMail> getByIdWithPotentialStudent(Long id) {
        List<PotentialStudentMail> resultList =
                entityManager.createNamedQuery(GET_POTENTIAL_STUDENT_MAIL_BY_ID,
                        PotentialStudentMail.class)
                        .setParameter("id", id)
                        .getResultList();

        return resultList.isEmpty() ? Optional.empty() : Optional.ofNullable(resultList.get(0));
    }

    @Override
    public List<PotentialStudentMail> getAll() {
        return entityManager
                .createNamedQuery(GET_ALL_POTENTIAL_STUDENTS_BY_MAIL,
                        PotentialStudentMail.class)
                .getResultList();
    }

    @Override
    public PotentialStudentMail save(PotentialStudentMail PotentialStudentMail) {
        entityManager.persist(PotentialStudentMail);
        return PotentialStudentMail;
    }

    @Override
    public Optional<PotentialStudentMail> update(PotentialStudentMail newPsMail, Long id) {
        PotentialStudent oldPS = studentService.getById(id)
                .orElseThrow(NotFoundException::new);

        studentService.updatePS(oldPS, newPsMail.getPotentialStudent());

        PotentialStudentMail oldPsMail = getById(id)
                .orElseThrow(NotFoundException::new);

        updatePSMail(oldPsMail, newPsMail);

        return Optional.ofNullable(entityManager.merge(oldPsMail));
    }

    @Override
    public void delete(Long id) {
        PotentialStudentMail PotentialStudentMailFromDB = getById(id)
                .orElseThrow(NotFoundException::new);

        entityManager.remove(PotentialStudentMailFromDB);
    }

    public PotentialStudentMail createPSByMail(String email, String phone, String fullName,
                                               String dateMailReceived, String dateMailReceivedOnUpis,
                                               String emailWhichReceived, String dateReply, BigDecimal price) {

        PotentialStudentMail psMail = new PotentialStudentMail();

        psMail.setPotentialStudent(
                studentService.createPotentialStudent(email, phone, fullName));

        if (!dateMailReceived.trim().isEmpty()) {
            psMail.setDateMailReceived(LocalDate.parse(dateMailReceived));
        }

        if (!dateMailReceivedOnUpis.trim().isEmpty()) {
            psMail.setGetDateMailReceivedOnUpis(LocalDate.parse(dateMailReceivedOnUpis));
        }

        if (!dateReply.trim().isEmpty()) {
            psMail.setDateReplay(LocalDate.parse(dateReply));
        }

        psMail.setEmailWhichReceived(emailWhichReceived);
        psMail.setPrice(price);

        return psMail;
    }

    private void updatePSMail(PotentialStudentMail oldPSMail, PotentialStudentMail newPSMail) {
        // maybe check which values are the same and then set only changed value

        oldPSMail.setDateMailReceived(newPSMail.getDateMailReceived());
        oldPSMail.setGetDateMailReceivedOnUpis(newPSMail.getGetDateMailReceivedOnUpis());
        oldPSMail.setDateReplay(newPSMail.getDateReplay());
        oldPSMail.setEmailWhichReceived(newPSMail.getEmailWhichReceived());
        oldPSMail.setPrice(newPSMail.getPrice());
    }
}
