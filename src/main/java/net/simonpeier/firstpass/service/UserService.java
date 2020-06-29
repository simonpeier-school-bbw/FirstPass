package net.simonpeier.firstpass.service;

import net.simonpeier.firstpass.model.User;
import net.simonpeier.firstpass.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.List;

@Service
@SessionScope
public class UserService {
    private final UserRepository userRepository;
    private User authorisedUser;
    private String secretKey;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findUserById(long id) {
        return userRepository.findById(id).get();
    }

    public User findUserByName(String name) {
        for (User user : findAll()) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public User createUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    public void deleteUserById(long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
    }

    public User updateUserPassword(long id, User user) {
        User userToUpdate = getReferenceToUserById(id);
        userToUpdate.setPassword(user.getPassword());
        userToUpdate.setSalt(user.getSalt());
        return userRepository.save(userToUpdate);
    }

    private User getReferenceToUserById(long id) {
        return userRepository.getOne(id);
    }

    public User getAuthorisedUser() {
        return authorisedUser;
    }

    public void setAuthorisedUser(User authorisedUser) {
        this.authorisedUser = authorisedUser;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
