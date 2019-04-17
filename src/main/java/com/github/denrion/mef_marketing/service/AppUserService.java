package com.github.denrion.mef_marketing.service;

import com.github.denrion.mef_marketing.config.DuplicateUsernameException;
import com.github.denrion.mef_marketing.entity.AppUser;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.util.ByteSource;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.denrion.mef_marketing.entity.AppUser.GET_USER_BY_USERNAME;

@Stateless
@LocalBean
public class AppUserService implements GenericService<AppUser> {

    private static final String HASHED_PASSWORD_KEY = "hashedPassword";
    private static final String SALT_KEY = "salt";

    @Inject
    EntityManager entityManager;

    @Override
    public Optional<AppUser> getById(Long id) {
        return Optional.ofNullable(entityManager.find(AppUser.class, id));
    }

    @Override
    public List<AppUser> getAll() {
        return entityManager
                .createNamedQuery(AppUser.GET_ALL_USERS, AppUser.class)
                .getResultList();
    }

    @Override
    public AppUser save(AppUser user) {

        // TODO -> FIND A MORE EFFICIENT WAY TO DO THIS
        if (isUsernameAlreadyInUse(user.getUsername())) {
            throw new DuplicateUsernameException("This username already exists");
        }

        Map<String, String> credMap = hashPassword(user.getPassword());

        user.setPassword(credMap.get(HASHED_PASSWORD_KEY));
        user.setSalt(credMap.get(SALT_KEY));

        entityManager.persist(user);

        credMap.clear();

        return user;
    }

    @Override
    public Optional<AppUser> update(AppUser newUser, Long id) {
        AppUser oldUser = getById(id)
                .orElseThrow(NotFoundException::new);

        updateAdminUserFields(oldUser, newUser);

        return Optional.ofNullable(entityManager.merge(oldUser));
    }

    @Override
    public void delete(Long id) {
        AppUser user = getById(id)
                .orElseThrow(NotFoundException::new);

        entityManager.remove(user);
    }

    public boolean authenticateUser(String username, String password) {
        final Optional<AppUser> optional = getByUsername(username);

        if (!optional.isPresent()) {
            return false;
        }

        AppUser user = optional.get();

        return passwordsMatch(user.getPassword(), user.getSalt(), password);
    }

    private Optional<AppUser> getByUsername(String username) {
        List<AppUser> resultList = entityManager
                .createNamedQuery(GET_USER_BY_USERNAME,
                        AppUser.class)
                .setParameter("username", username)
                .getResultList();

        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
    }

    private boolean isUsernameAlreadyInUse(String username) {
        return getByUsername(username).isPresent();
    }

    private void updateAdminUserFields(AppUser oldUser, AppUser newUser) {
        if (!oldUser.getUsername().equals(newUser.getUsername())
                && isUsernameAlreadyInUse(newUser.getUsername())) {
            throw new DuplicateUsernameException("This username already exists");
        }

        oldUser.setUsername(newUser.getUsername());

        if (!passwordsMatch(oldUser.getPassword(), oldUser.getSalt(), newUser.getPassword())) {
            oldUser.setPassword(newUser.getPassword());
        }

        oldUser.setFullName(newUser.getFullName());
    }

    private Map<String, String> hashPassword(String clearTextPassword) {
        ByteSource salt = getSalt();

        final HashMap<String, String> credMap = new HashMap<>();
        credMap.put(HASHED_PASSWORD_KEY, hashAndSaltPassword(clearTextPassword, salt));
        credMap.put(SALT_KEY, salt.toHex());

        return credMap;
    }

    private boolean passwordsMatch(String dbStoredHashedPassword, String saltText, String clearTextPassword) {
        ByteSource salt = ByteSource.Util.bytes(Hex.decode(saltText));
        String hashedPassword = hashAndSaltPassword(clearTextPassword, salt);
        return hashedPassword.equals(dbStoredHashedPassword);
    }

    private String hashAndSaltPassword(String clearTextPassword, ByteSource salt) {
        return new Sha512Hash(clearTextPassword, salt, 2000000).toHex();
    }

    private ByteSource getSalt() {
        return new SecureRandomNumberGenerator().nextBytes();
    }
}
