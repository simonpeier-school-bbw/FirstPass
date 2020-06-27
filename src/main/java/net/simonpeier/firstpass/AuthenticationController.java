package net.simonpeier.firstpass;

import net.simonpeier.firstpass.Model.User;
import net.simonpeier.firstpass.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthenticationController {
    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute(new User());
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(Model model, @ModelAttribute User loginUser) {
        if (userService.getUserByName(loginUser.getUsername()) != null) {
            if (userService.getUserByName(loginUser.getUsername()).getPassword().equals(loginUser.getPassword())) {
                // login successful
                User user = userService.getUserByName(loginUser.getUsername());
                model.addAttribute(user);
                return "dashboard";
            }
        }
        model.addAttribute("error", "Wrong username or password");
        return "login";
    }
}
