package net.simonpeier.firstpass.service;

import net.simonpeier.firstpass.model.User;
import net.simonpeier.firstpass.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    public void deleteUserById(long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
    }

    public User updateUser(User user, int id) {
        User updatedUser;
        Optional<User> optionalUpdatedEntry = findUserById(id);

        if (optionalUpdatedEntry.isPresent()) {
            updatedUser = optionalUpdatedEntry.get();
            updatedUser.setUsername(user.getUsername());
        } else {
            updatedUser = user;
            updatedUser.setId(id);
        }
        return userRepository.saveAndFlush(updatedUser);
    }
}
