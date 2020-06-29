package net.simonpeier.firstpass.controller;

import net.simonpeier.firstpass.model.Application;
import net.simonpeier.firstpass.model.User;
import net.simonpeier.firstpass.security.Cypher;
import net.simonpeier.firstpass.service.ApplicationService;
import net.simonpeier.firstpass.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
                userService.setAuthorisedUser(referenceUser);
                return "redirect:/dashboard";
            }
        }
        model.addAttribute("error", "Wrong username or password");
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        User user = userService.getAuthorisedUser();
        if (user != null) {
            User referenceUser = userService.findUserByName(user.getUsername());
            model.addAttribute("user", referenceUser);
            model.addAttribute("applications", applicationService.findAllByUser(referenceUser));
            return "/dashboard";
        }
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout() {
        userService.setAuthorisedUser(null);
        return "redirect:/";
    }

    @GetMapping("/add-application")
    public String addReservation(Model model) {
        model.addAttribute(new Application());
        return "add-application";
    }

    @PostMapping("/add-application")
    public String addReservation(@ModelAttribute Application application) {
        application.setUser(userService.findUserByName(userService.getAuthorisedUser().getUsername()));
        applicationService.createApplication(application);
        return "redirect:/dashboard";
    }

    @GetMapping("/edit-application/{id}")
    public String editReservation(@PathVariable("id") long id, Model model) {
        model.addAttribute("app", applicationService.findApplicationById(id));
        System.out.println();
        return "edit-application";
    }

    @PostMapping("/edit-application/{id}")
    public String editReservation(@PathVariable("id") long id, @ModelAttribute Application application) {
        System.out.println(id);
        applicationService.updateApplication(application, application.getId());
        return "redirect:/dashboard";
    }

    @GetMapping("/delete-application/{id}")
    public String deleteReservation(@PathVariable("id") long id) {
        applicationService.deleteApplicationById(id);
        return "redirect:/dashboard";
    }
}
