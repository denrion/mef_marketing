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

    public PotentialStudent createPotentialStudent(String email, String phone, String fullName) {
        PotentialStudent student = new PotentialStudent();

        student.setEmail(email);
        student.setPhone(phone);
        student.setFullName(fullName);

        return student;
    }

    public void updatePSFields(PotentialStudent oldPS, PotentialStudent newPS) {
        if (oldPS.getEmail().equals(newPS.getEmail()) || getByEmail(newPS.getEmail()).isPresent()) {
            throw new DuplicateEmailException("This email already exists");
        } else {
            oldPS.setEmail(newPS.getEmail());
        }

        if (!oldPS.getPhone().equals(newPS.getPhone()))
            oldPS.setPhone(newPS.getPhone());

        if (!oldPS.getFullName().equals(newPS.getEmail()))
            oldPS.setFullName(newPS.getFullName());
    }
}
