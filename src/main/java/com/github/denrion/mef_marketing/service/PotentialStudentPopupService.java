package com.github.denrion.mef_marketing.service;

import com.github.denrion.mef_marketing.entity.PotentialStudentPopup;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Stateless
@LocalBean
public class PotentialStudentPopupService implements GenericService<PotentialStudentPopup> {

    @Inject
    EntityManager entityManager;

    @Inject
    PotentialStudentService studentService;

    @Override
    public Optional<PotentialStudentPopup> getById(Long id) {
        List<PotentialStudentPopup> list = entityManager
                .createNamedQuery(PotentialStudentPopup.GET_POTENTIAL_STUDENT_POPUP_BY_ID,
                        PotentialStudentPopup.class)
                .setParameter("id", id)
                .getResultList();

        return list.isEmpty() ? Optional.empty() : Optional.ofNullable(list.get(0));
    }

    @Override
    public List<PotentialStudentPopup> getAll() {
        return entityManager
                .createNamedQuery(PotentialStudentPopup.GET_ALL_POTENTIAL_STUDENTS_POPUP,
                        PotentialStudentPopup.class)
                .getResultList();
    }

    @Override
    public PotentialStudentPopup save(PotentialStudentPopup psPopup) {
        studentService.checkIfEmailAlreadyInDB(psPopup.getPotentialStudent().getEmail());

        entityManager.persist(psPopup);

        return psPopup;
    }

    @Override
    public Optional<PotentialStudentPopup> update(PotentialStudentPopup newPSPopup, Long id) {
        PotentialStudentPopup oldPSPopup = getById(id)
                .orElseThrow(NotFoundException::new);

        studentService.updatePSFields(oldPSPopup.getPotentialStudent(), newPSPopup.getPotentialStudent());
        updatePSPopupFields(oldPSPopup, newPSPopup);

        return Optional.ofNullable(entityManager.merge(oldPSPopup));
    }

    @Override
    public void delete(Long id) {
        PotentialStudentPopup psPopup = getById(id)
                .orElseThrow(NotFoundException::new);

        entityManager.remove(psPopup);
    }

    // FOR TESTING

    public PotentialStudentPopup createPSPopup(String email, String phone, String fullName,
                                               String dateContact, String dateSignUp) {

        PotentialStudentPopup psPopup = new PotentialStudentPopup();

        psPopup.setPotentialStudent(studentService
                .createPotentialStudent(email, phone, fullName));

        if (!dateContact.trim().isEmpty()) {
            psPopup.setDateContact(LocalDate.parse(dateContact));
        }

        if (!dateSignUp.trim().isEmpty()) {
            psPopup.setDateSignUp(LocalDate.parse(dateSignUp));
        }

        return psPopup;
    }

    private void updatePSPopupFields(PotentialStudentPopup oldPSPopup, PotentialStudentPopup newPSPopup) {
        oldPSPopup.setDateContact(newPSPopup.getDateContact());
        oldPSPopup.setDateSignUp(newPSPopup.getDateSignUp());
    }
}
