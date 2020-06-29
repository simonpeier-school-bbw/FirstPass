package net.simonpeier.firstpass;

import net.simonpeier.firstpass.model.User;
import net.simonpeier.firstpass.service.ApplicationService;
import net.simonpeier.firstpass.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Controller
public class AuthenticationController {
    final UserService userService;
    final ApplicationService applicationService;
    private final Cypher cypher;

    public AuthenticationController(UserService userService, ApplicationService applicationService) {
        this.userService = userService;
        this.applicationService = applicationService;
        cypher = new Cypher();
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute(new User());
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(Model model, @ModelAttribute User user) throws InvalidKeySpecException, NoSuchAlgorithmException {
        User referenceUser = userService.findUserByName(user.getUsername());
        if (referenceUser != null) {
            user.setPassword(cypher.hashPassword(user.getPassword(), referenceUser.getSalt()));
            if (userService.findUserByName(user.getUsername()).getPassword().equals(user.getPassword())) {
                // login successful
                userService.setAuthorisedUser(user);
                model.addAttribute("user", user);
                model.addAttribute("applications", applicationService.findAllByUser(referenceUser));
                return "dashboard";
            }
        }
        model.addAttribute("error", "Wrong username or password");
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        userService.setAuthorisedUser(null);
        return "redirect:/";
    }
}
