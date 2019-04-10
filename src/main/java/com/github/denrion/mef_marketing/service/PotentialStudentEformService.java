package com.github.denrion.mef_marketing.service;

import com.github.denrion.mef_marketing.entity.PotentialStudentEform;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

import static com.github.denrion.mef_marketing.entity.PotentialStudentEform.GET_ALL_POTENTIAL_STUDENTS_EFORM;
import static com.github.denrion.mef_marketing.entity.PotentialStudentEform.GET_POTENTIAL_STUDENT_EFORM_BY_ID;

@Stateless
@LocalBean
public class PotentialStudentEformService implements GenericService<PotentialStudentEform> {

    @Inject
    EntityManager entityManager;

    @Inject
    PotentialStudentService studentService;

    @Override
    public Optional<PotentialStudentEform> getById(Long id) {
        List<PotentialStudentEform> list = entityManager
                .createNamedQuery(GET_POTENTIAL_STUDENT_EFORM_BY_ID,
                        PotentialStudentEform.class)
                .setParameter("id", id)
                .getResultList();

        return list.isEmpty() ? Optional.empty() : Optional.ofNullable(list.get(0));
    }

    @Override
    public List<PotentialStudentEform> getAll() {
        return entityManager
                .createNamedQuery(GET_ALL_POTENTIAL_STUDENTS_EFORM,
                        PotentialStudentEform.class)
                .getResultList();
    }

    @Override
    public PotentialStudentEform save(PotentialStudentEform pse) {
        studentService.checkIfEmailAlreadyInDB(pse.getPotentialStudent().getEmail());

        entityManager.persist(pse);

        return pse;
    }

    @Override
    public Optional<PotentialStudentEform> update(PotentialStudentEform newPSEform, Long id) {
        PotentialStudentEform oldPSEform = getById(id)
                .orElseThrow(NotFoundException::new);

        studentService.updatePSFields(oldPSEform.getPotentialStudent(), newPSEform.getPotentialStudent());
        updatePSEformFields(oldPSEform, newPSEform);

        return Optional.ofNullable(entityManager.merge(oldPSEform));
    }

    @Override
    public void delete(Long id) {
        PotentialStudentEform psEform = getById(id)
                .orElseThrow(NotFoundException::new);

        entityManager.remove(psEform);
    }

    private void updatePSEformFields(PotentialStudentEform oldPSEform, PotentialStudentEform newPSEform) {
        oldPSEform.setDateContact(newPSEform.getDateContact());
        oldPSEform.setDateSignUp(newPSEform.getDateSignUp());
    }


    // FOR TESTING

//    public PotentialStudentEform createPSEform(String email, String phone, String fullName,
//                                               String dateContact, String dateSignUp) {
//
//        PotentialStudentEform psEform = new PotentialStudentEform();
//
//        psEform.setPotentialStudent(studentService
//                .createPotentialStudent(email, phone, fullName));
//
//        if (!dateContact.trim().isEmpty()) {
//            psEform.setDateContact(LocalDate.parse(dateContact));
//        }
//
//        if (!dateSignUp.trim().isEmpty()) {
//            psEform.setDateSignUp(LocalDate.parse(dateSignUp));
//        }
//
//        return psEform;
//    }
}
