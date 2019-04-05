package com.github.denrion.mef_marketing.service;

import com.github.denrion.mef_marketing.entity.AdminUser;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

@Stateless
@LocalBean
public class AdminUserService implements GenericService<AdminUser> {

    @Inject
    EntityManager entityManager;

    @Override
    public List<AdminUser> getAll() {
        return entityManager
                .createNamedQuery(AdminUser.GET_ALL_USERS, AdminUser.class)
                .getResultList();
    }

    @Override
    public Optional<AdminUser> getById(Long id) {
        return Optional.ofNullable(
                entityManager.find(AdminUser.class, id));
    }

    @Override
    public AdminUser save(AdminUser user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    public Optional<AdminUser> update(AdminUser user, Long id) {
        AdminUser adminUser = getById(id)
                .orElseThrow(NotFoundException::new);

        // TODO check if username already exists in DB

        adminUser.setUsername(user.getUsername());
        adminUser.setPassword(user.getPassword());
        adminUser.setFullName(user.getFullName());

        return Optional.ofNullable(
                entityManager.merge(user));
    }

    @Override
    public void delete(Long id) {
        AdminUser user = getById(id)
                .orElseThrow(NotFoundException::new);

        entityManager.remove(user);
    }
}
