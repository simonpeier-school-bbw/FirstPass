package net.simonpeier.firstpass;

import net.simonpeier.firstpass.model.User;
import net.simonpeier.firstpass.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthenticationController {
    final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute(new User());
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(Model model, @ModelAttribute User loginUser) {
        if (userService.findUserByName(loginUser.getUsername()) != null) {
            if (userService.findUserByName(loginUser.getUsername()).getPassword().equals(loginUser.getPassword())) {
                // login successful
                User user = userService.findUserByName(loginUser.getUsername());
                model.addAttribute(user);
                return "dashboard";
            }
        }
        model.addAttribute("error", "Wrong username or password");
        return "login";
    }
}
