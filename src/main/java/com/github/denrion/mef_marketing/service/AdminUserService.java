package com.github.denrion.mef_marketing.service;

import com.github.denrion.mef_marketing.config.DuplicateUsernameException;
import com.github.denrion.mef_marketing.entity.AdminUser;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

import static com.github.denrion.mef_marketing.entity.AdminUser.GET_USER_BY_USERNAME;

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
        return Optional.ofNullable(entityManager.find(AdminUser.class, id));
    }

    @Override
    public AdminUser save(AdminUser user) {

        // TODO -> FIND A MORE EFFICIENT WAY TO DO THIS
        if (isUsernameAlreadyInUse(user.getUsername())) {
            throw new DuplicateUsernameException("This username already exists");
        }

        entityManager.persist(user);

        return user;
    }

    @Override
    public Optional<AdminUser> update(AdminUser newUser, Long id) {
        AdminUser oldUser = getById(id)
                .orElseThrow(NotFoundException::new);

        updateAdminUser(oldUser, newUser);

        return Optional.ofNullable(entityManager.merge(oldUser));
    }

    @Override
    public void delete(Long id) {
        AdminUser user = getById(id)
                .orElseThrow(NotFoundException::new);

        entityManager.remove(user);
    }

    private Optional<AdminUser> getByUsername(String username) {
        List<AdminUser> resultList = entityManager
                .createNamedQuery(GET_USER_BY_USERNAME,
                        AdminUser.class)
                .setParameter("username", username)
                .getResultList();

        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
    }

    private boolean isUsernameAlreadyInUse(String username) {
        return getByUsername(username).isPresent();
    }

    private void updateAdminUser(AdminUser oldUser, AdminUser newUser) {
        if (!oldUser.getUsername().equals(newUser.getUsername())
                && isUsernameAlreadyInUse(newUser.getUsername())) {
            throw new DuplicateUsernameException("This username already exists");
        }

        oldUser.setUsername(newUser.getUsername());
        oldUser.setPassword(newUser.getPassword());
        oldUser.setFullName(newUser.getFullName());
    }
}
