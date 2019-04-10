package com.github.denrion.mef_marketing.service;

import com.github.denrion.mef_marketing.config.DuplicateEmailException;
import com.github.denrion.mef_marketing.entity.PotentialStudent;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.github.denrion.mef_marketing.entity.PotentialStudent.GET_POTENTIAL_STUDENT_BY_EMAIL;

@Stateless
@LocalBean
public class PotentialStudentService {

    @Inject
    EntityManager entityManager;

    public Optional<PotentialStudent> getById(Long id) {
        return Optional.ofNullable(entityManager.find(PotentialStudent.class, id));
    }

    // TODO -> FIND A MORE EFFICIENT WAY TO DO THIS
    public void checkIfEmailAlreadyInDB(String email) {
        if (getByEmail(email).isPresent()) {
            throw new DuplicateEmailException("This email already exists");
        }
    }

    private Optional<PotentialStudent> getByEmail(String email) {
        List<PotentialStudent> resultList = entityManager
                .createNamedQuery(GET_POTENTIAL_STUDENT_BY_EMAIL,
                        PotentialStudent.class)
                .setParameter("email", email)
                .getResultList();

        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
    }

    public void updatePSFields(PotentialStudent oldPS, PotentialStudent newPS) {
        if (!oldPS.getEmail().equals(newPS.getEmail())
                && getByEmail(newPS.getEmail()).isPresent()) {
            throw new DuplicateEmailException("This email already exists");
        }

        oldPS.setEmail(newPS.getEmail());
        oldPS.setPhone(newPS.getPhone());
        oldPS.setFullName(newPS.getFullName());
    }


    // FOR TESTING

//    public PotentialStudent createPotentialStudent(String email, String phone, String fullName) {
//        PotentialStudent student = new PotentialStudent();
//
//        student.setEmail(email);
//        student.setPhone(phone);
//        student.setFullName(fullName);
//
//        return student;
//    }
}
