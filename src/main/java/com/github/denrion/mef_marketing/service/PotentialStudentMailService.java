package com.github.denrion.mef_marketing.service;

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

import static com.github.denrion.mef_marketing.entity.PotentialStudentMail.GET_ALL_POTENTIAL_STUDENTS_MAIL;
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
//        return Optional.ofNullable(entityManager.find(PotentialStudentMail.class, id));

        // TODO -> only 1 query, is it more efficient???!!
        List<PotentialStudentMail> list = entityManager
                .createNamedQuery(GET_POTENTIAL_STUDENT_MAIL_BY_ID,
                        PotentialStudentMail.class)
                .setParameter("id", id)
                .getResultList();

        return list.isEmpty() ? Optional.empty() : Optional.ofNullable(list.get(0));
    }

    @Override
    public List<PotentialStudentMail> getAll() {
        return entityManager
                .createNamedQuery(GET_ALL_POTENTIAL_STUDENTS_MAIL,
                        PotentialStudentMail.class)
                .getResultList();
    }

    @Override
    public PotentialStudentMail save(PotentialStudentMail psm) {
        studentService.checkIfEmailAlreadyInDB(psm.getPotentialStudent().getEmail());

        entityManager.persist(psm);

        return psm;
    }

    @Override
    public Optional<PotentialStudentMail> update(PotentialStudentMail newPSMail, Long id) {
        PotentialStudentMail oldPSMail = getById(id)
                .orElseThrow(NotFoundException::new);

        studentService.updatePSFields(oldPSMail.getPotentialStudent(), newPSMail.getPotentialStudent());
        updatePSMailFields(oldPSMail, newPSMail);

        return Optional.ofNullable(entityManager.merge(oldPSMail));
    }

    @Override
    public void delete(Long id) {
        PotentialStudentMail psMail = getById(id)
                .orElseThrow(NotFoundException::new);

        entityManager.remove(psMail);
    }

    // FOR TESTING

    public PotentialStudentMail createPSMail(String email, String phone, String fullName,
                                             String dateMailReceived, String dateMailReceivedOnUpis,
                                             String emailWhichReceived, String dateReply, BigDecimal price) {

        PotentialStudentMail psMail = new PotentialStudentMail();

        psMail.setPotentialStudent(studentService
                .createPotentialStudent(email, phone, fullName));

        if (!dateMailReceived.trim().isEmpty()) {
            psMail.setDateMailReceived(LocalDate.parse(dateMailReceived));
        }

        if (!dateMailReceivedOnUpis.trim().isEmpty()) {
            psMail.setDateMailReceivedOnUpis(LocalDate.parse(dateMailReceivedOnUpis));
        }

        if (!dateReply.trim().isEmpty()) {
            psMail.setDateReply(LocalDate.parse(dateReply));
        }

        psMail.setEmailWhichReceived(emailWhichReceived);
        psMail.setPrice(price);

        return psMail;
    }

    private void updatePSMailFields(PotentialStudentMail oldPSMail, PotentialStudentMail newPSMail) {
        // TODO -> SHOULD I CHECK IF VALUES ARE THE SAME FIRST???!!!!

        oldPSMail.setDateMailReceived(newPSMail.getDateMailReceived());
        oldPSMail.setDateMailReceivedOnUpis(newPSMail.getDateMailReceivedOnUpis());
        oldPSMail.setDateReply(newPSMail.getDateReply());
        oldPSMail.setEmailWhichReceived(newPSMail.getEmailWhichReceived());
        oldPSMail.setPrice(newPSMail.getPrice());
    }
}
