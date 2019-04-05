package com.github.denrion.mef_marketing.service;

import com.github.denrion.mef_marketing.entity.AdminUser;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@Stateless
public class AdminUserService {

    @Inject
    EntityManager entityManager;

    public List<AdminUser> getAll() {
        return entityManager
                .createNamedQuery(AdminUser.GET_ALL_USERS, AdminUser.class)
                .getResultList();
    }

    public AdminUser getById(Long id) {
        return entityManager.find(AdminUser.class, id);
    }
}
