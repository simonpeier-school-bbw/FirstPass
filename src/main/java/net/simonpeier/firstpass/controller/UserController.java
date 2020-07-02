package net.simonpeier.firstpass.controller;

import net.simonpeier.firstpass.model.Application;
import net.simonpeier.firstpass.model.User;
import net.simonpeier.firstpass.security.Cypher;
import net.simonpeier.firstpass.service.ApplicationService;
import net.simonpeier.firstpass.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;
    private final ApplicationService applicationService;
    private final Cypher cypher;

    public UserController(UserService userService, ApplicationService applicationService) {
        this.userService = userService;
        this.applicationService = applicationService;
        cypher = new Cypher();
    }

    @GetMapping("/edit-password/{id}")
    public String editPassword(@PathVariable("id") long id, Model model) {
        if (userService.getAuthorisedUser() != null) {
            model.addAttribute("user", userService.findUserById(id));
            return "edit-password";
        }
        return "redirect:/login";
    }

    @PostMapping("/edit-password/{id}")
    public String editPassword(@PathVariable("id") long id, @ModelAttribute User user) throws InvalidKeySpecException, NoSuchAlgorithmException {
        User oldUser = userService.findUserById(id);

        user.setUsername(oldUser.getUsername());
        if (userService.getAuthorisedUser() != null) {
            // Generate new salt
            String salt = cypher.generateSalt();
            // Set salt of user
            user.setSalt(salt);
            // Encrypt password of user
            user.setPassword(cypher.hashPassword(user.getPassword(), salt));
            // Save changes into database
            userService.updateUserPassword(id, user);
            userService.setAuthorisedUser(userService.findUserById(id));

            // Encrypt data with new SecretKey
            String newSecretKey = user.getPassword();
            List<Application> applications = applicationService.findAllByUser(oldUser);
            for (Application application : applications) {
                applicationService.updateApplication(application.getId(), application, userService.getAuthorisedUser(), newSecretKey);
            }
            userService.setSecretKey(newSecretKey);

            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }
}
