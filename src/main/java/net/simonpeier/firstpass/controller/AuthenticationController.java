package net.simonpeier.firstpass.controller;

import net.simonpeier.firstpass.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {
    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/users")
    public String showUser(Model model) {
        model.addAttribute("userList", userService.findAll());
        return "users";
    }
}
