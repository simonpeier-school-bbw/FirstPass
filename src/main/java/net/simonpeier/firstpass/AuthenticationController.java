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
    private Cypher cypher;

    public AuthenticationController(UserService userService, ApplicationService applicationService) {
        this.userService = userService;
        this.applicationService = applicationService;
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute(new User());
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(Model model, @ModelAttribute User loginUser) throws InvalidKeySpecException, NoSuchAlgorithmException {
        User referenceUser = userService.findUserByName(loginUser.getUsername());
        if (referenceUser != null) {
            loginUser.setPassword(cypher.hashPassword(loginUser, referenceUser.getSalt()));
            if (userService.findUserByName(loginUser.getUsername()).getPassword().equals(loginUser.getPassword())) {
                // login successful
                User user = userService.findUserByName(loginUser.getUsername());
                model.addAttribute("user", user);
                model.addAttribute("applications", applicationService.findAllByUser(user));
                return "dashboard";
            }
        }
        model.addAttribute("error", "Wrong username or password");
        return "login";
    }
}
