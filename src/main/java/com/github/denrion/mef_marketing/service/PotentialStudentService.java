package com.github.denrion.mef_marketing.service;

import com.github.denrion.mef_marketing.config.DuplicateEmailException;
import com.github.denrion.mef_marketing.entity.PotentialStudent;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

import static com.github.denrion.mef_marketing.entity.PotentialStudent.GET_ALL_POTENTIAL_STUDENTS;
import static com.github.denrion.mef_marketing.entity.PotentialStudent.GET_POTENTIAL_STUDENT_BY_EMAIL;

@Stateless
@LocalBean
public class PotentialStudentService implements GenericService<PotentialStudent> {

    @Inject
    EntityManager entityManager;

    @Override
    public Optional<PotentialStudent> getById(Long id) {
        return Optional.ofNullable(
                entityManager.find(PotentialStudent.class, id));
    }

    @Override
    public List<PotentialStudent> getAll() {
        return entityManager
                .createNamedQuery(GET_ALL_POTENTIAL_STUDENTS, PotentialStudent.class)
                .getResultList();
    }

    @Override
    public PotentialStudent save(PotentialStudent potentialStudent) {
        entityManager.persist(potentialStudent);
        return potentialStudent;
    }

    @Override
    public Optional<PotentialStudent> update(PotentialStudent potentialStudent, Long id) {
        PotentialStudent studentFromDB = getById(id)
                .orElseThrow(NotFoundException::new);

        if (studentFromDB.getEmail().equals(potentialStudent.getEmail())) {
            throw new DuplicateEmailException("This email is already in use");
        }

        studentFromDB.setFullName(potentialStudent.getFullName());
        studentFromDB.setEmail(potentialStudent.getEmail());
        studentFromDB.setPhone(potentialStudent.getPhone());

        return Optional.ofNullable(entityManager.merge(studentFromDB));
    }

    @Override
    public void delete(Long id) {
        PotentialStudent student = getById(id)
                .orElseThrow(NotFoundException::new);

        entityManager.remove(student);
    }

    public Optional<PotentialStudent> getByEmail(String email) {
        List<PotentialStudent> resultList = entityManager
                .createNamedQuery(GET_POTENTIAL_STUDENT_BY_EMAIL,
                        PotentialStudent.class)
                .setParameter("email", email)
                .getResultList();

        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
    }

    public boolean isEmailAlreadyInUse(String email) {
        return getByEmail(email).isPresent();
    }

    public PotentialStudent createPotentialStudent(String email, String phone, String fullName) {
        PotentialStudent student = new PotentialStudent();

        student.setEmail(email);
        student.setPhone(phone);
        student.setFullName(fullName);

        return student;
    }

    public void updatePS(PotentialStudent oldPS, PotentialStudent newPS) {
        oldPS.setEmail(newPS.getEmail());
        oldPS.setPhone(newPS.getPhone());
        oldPS.setFullName(newPS.getFullName());
    }

}
