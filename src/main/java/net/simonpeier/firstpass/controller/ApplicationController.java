package net.simonpeier.firstpass.controller;

import net.simonpeier.firstpass.model.Application;
import net.simonpeier.firstpass.model.User;
import net.simonpeier.firstpass.service.ApplicationService;
import net.simonpeier.firstpass.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ApplicationController {
    private final UserService userService;
    private final ApplicationService applicationService;

    public ApplicationController(UserService userService, ApplicationService applicationService) {
        this.userService = userService;
        this.applicationService = applicationService;
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

    @GetMapping("/add-application")
    public String addApplication(Model model) {
        if (userService.getAuthorisedUser() != null) {
            model.addAttribute("app", new Application());
            return "add-application";
        }
        return "redirect:/login";
    }

    @PostMapping("/add-application")
    public String addApplication(@ModelAttribute Application application) {
        if (userService.getAuthorisedUser() != null) {
            application.setUser(userService.findUserByName(userService.getAuthorisedUser().getUsername()));
            applicationService.createApplication(application);
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }

    @GetMapping("/edit-application/{id}")
    public String editApplication(@PathVariable("id") long id, Model model) {
        if (userService.getAuthorisedUser() != null) {
            model.addAttribute("app", applicationService.findApplicationById(id));
            return "edit-application";
        }
        return "redirect:/login";
    }

    @PostMapping("/edit-application/{id}")
    public String editApplication(@PathVariable("id") long id, @ModelAttribute Application application) {
        if (userService.getAuthorisedUser() != null) {
            applicationService.updateApplication(id, application, userService.getAuthorisedUser(), userService.getSecretKey());
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }

    @GetMapping("/delete-application/{id}")
    public String deleteApplication(@PathVariable("id") long id) {
        if (userService.getAuthorisedUser() != null) {
            applicationService.deleteApplicationById(id);
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }
}
