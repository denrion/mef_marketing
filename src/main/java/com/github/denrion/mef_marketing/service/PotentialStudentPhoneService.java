package com.github.denrion.mef_marketing.service;

import com.github.denrion.mef_marketing.config.DuplicateEmailException;
import com.github.denrion.mef_marketing.entity.PotentialStudentPhone;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.github.denrion.mef_marketing.entity.PotentialStudentPhone.GET_ALL_POTENTIAL_STUDENTS_PHONE;
import static com.github.denrion.mef_marketing.entity.PotentialStudentPhone.GET_POTENTIAL_STUDENT_PHONE_BY_ID;

@Stateless
@LocalBean
public class PotentialStudentPhoneService implements GenericService<PotentialStudentPhone> {

    @Inject
    EntityManager entityManager;

    @Inject
    PotentialStudentService studentService;

    @Override
    public Optional<PotentialStudentPhone> getById(Long id) {
        // TODO -> only 1 query, is it more efficient???!!
        List<PotentialStudentPhone> list = entityManager
                .createNamedQuery(GET_POTENTIAL_STUDENT_PHONE_BY_ID,
                        PotentialStudentPhone.class)
                .setParameter("id", id)
                .getResultList();

        return list.isEmpty() ? Optional.empty() : Optional.ofNullable(list.get(0));
    }

    @Override
    public List<PotentialStudentPhone> getAll() {
        return entityManager
                .createNamedQuery(GET_ALL_POTENTIAL_STUDENTS_PHONE,
                        PotentialStudentPhone.class)
                .getResultList();
    }

    @Override
    public PotentialStudentPhone save(PotentialStudentPhone psp) {
        // TODO -> FIND A MORE EFFICIENT WAY TO DO THIS
        if (studentService.isEmailAlreadyInDB(psp.getPotentialStudent().getEmail())) {
            throw new DuplicateEmailException("This email already exists");
        }

        entityManager.persist(psp);

        return psp;
    }

    @Override
    public Optional<PotentialStudentPhone> update(PotentialStudentPhone newPSPhone, Long id) {
        PotentialStudentPhone oldPSPhone = getById(id)
                .orElseThrow(NotFoundException::new);

        studentService.updatePSFields(oldPSPhone.getPotentialStudent(), newPSPhone.getPotentialStudent());
        updatePSPhoneFields(oldPSPhone, newPSPhone);

        return Optional.ofNullable(entityManager.merge(oldPSPhone));
    }

    @Override
    public void delete(Long id) {
        PotentialStudentPhone psPhone = getById(id)
                .orElseThrow(NotFoundException::new);

        entityManager.remove(psPhone);
    }

    // FOR TESTING
    public PotentialStudentPhone createPSPhone(String email, String phone, String fullName,
                                               String city, String heardOf, int studyYear,
                                               String dateVisit, BigDecimal price, String whoCalledMef,
                                               String enrollment) {

        PotentialStudentPhone psPhone = new PotentialStudentPhone();

        psPhone.setPotentialStudent(studentService
                .createPotentialStudent(email, phone, fullName));

        psPhone.setCity(city);
        psPhone.setHeardOf(heardOf);

        psPhone.setStudyYear(studyYear);

        if (!dateVisit.trim().isEmpty()) {
            psPhone.setDateVisit(LocalDate.parse(dateVisit));
        }

        psPhone.setPrice(price);
        psPhone.setWhoCalledMef(whoCalledMef);
        psPhone.setEnrollment(enrollment);

        psPhone.setPrice(price);

        return psPhone;
    }

    private void updatePSPhoneFields(PotentialStudentPhone oldPSPhone, PotentialStudentPhone newPSPhone) {
        // TODO -> SHOULD I CHECK IF VALUES ARE THE SAME FIRST???!!!!

        oldPSPhone.setCity(newPSPhone.getCity());
        oldPSPhone.setHeardOf(newPSPhone.getHeardOf());
        oldPSPhone.setStudyYear(newPSPhone.getStudyYear());
        oldPSPhone.setDateVisit(newPSPhone.getDateVisit());
        oldPSPhone.setPrice(newPSPhone.getPrice());
        oldPSPhone.setWhoCalledMef(newPSPhone.getWhoCalledMef());
        oldPSPhone.setEnrollment(newPSPhone.getEnrollment());
    }
}
