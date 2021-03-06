package com.github.denrion.mef_marketing.service;

import com.github.denrion.mef_marketing.entity.EntranceTest;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

@Stateless
@LocalBean
public class EntranceTestService implements GenericService<EntranceTest> {

    @Inject
    EntityManager entityManager;

    @Override
    public Optional<EntranceTest> getById(Long id) {
        List<EntranceTest> list = entityManager
                .createNamedQuery(EntranceTest.GET_ENTRANCE_TEST_BY_ID,
                        EntranceTest.class)
                .setParameter("id", id)
                .getResultList();

        return list.isEmpty() ? Optional.empty() : Optional.ofNullable(list.get(0));
    }

    @Override
    public List<EntranceTest> getAll() {
        return entityManager
                .createNamedQuery(EntranceTest.GET_ALL_ENTRANCE_TESTS,
                        EntranceTest.class)
                .getResultList();
    }

    @Override
    public EntranceTest save(EntranceTest test) {
        // TODO check if test already exists in the DB
        entityManager.persist(test);

        return test;
    }

    @Override
    public Optional<EntranceTest> update(EntranceTest newTest, Long id) {
        EntranceTest oldTest = getById(id)
                .orElseThrow(NotFoundException::new);

        updateTestFields(oldTest, newTest);

        return Optional.ofNullable(entityManager.merge(oldTest));
    }

    @Override
    public void delete(Long id) {
        EntranceTest test = getById(id)
                .orElseThrow(NotFoundException::new);

        entityManager.remove(test);
    }

    private void updateTestFields(EntranceTest oldTest, EntranceTest newTest) {
        oldTest.setComment(newTest.getComment());
        oldTest.setDateDeal(newTest.getDateDeal());
        oldTest.setDateMailSent(newTest.getDateMailSent());
        oldTest.setMailContent(newTest.getMailContent());
        oldTest.setPotentialStudent(newTest.getPotentialStudent());
        oldTest.setUser(newTest.getUser());
    }

}
