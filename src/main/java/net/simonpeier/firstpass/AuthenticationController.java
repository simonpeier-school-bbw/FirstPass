package net.simonpeier.firstpass;

import net.simonpeier.firstpass.Model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthenticationController {

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute(new User());
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(Model model, @ModelAttribute User user) {
        if (user.getUsername().equals("Philipp") && user.getPassword().equals("asdf")) {
            return "dashboard";
        } else {
            model.addAttribute("error", "Wrong username or password");
            return "login";
        }
    }



}
