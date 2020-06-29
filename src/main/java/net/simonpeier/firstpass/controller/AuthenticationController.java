package net.simonpeier.firstpass.controller;

import net.simonpeier.firstpass.model.Application;
import net.simonpeier.firstpass.model.User;
import net.simonpeier.firstpass.security.Cypher;
import net.simonpeier.firstpass.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@Controller
public class AuthenticationController {
    private final UserService userService;
    private final Cypher cypher;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
        cypher = new Cypher();
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute(new User());
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(Model model, @ModelAttribute User user) throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        String plainPw = user.getPassword();
        User referenceUser = userService.findUserByName(user.getUsername());
        if (referenceUser != null) {
            user.setPassword(cypher.hashPassword(user.getPassword(), referenceUser.getSalt()));
            if (userService.findUserByName(user.getUsername()).getPassword().equals(user.getPassword())) {
                // login successful
                userService.setSecretKey(cypher.hashPassword(plainPw, cypher.generateSalt()));
                userService.setAuthorisedUser(referenceUser);
                List<Application> entriesDecrypted = cypher.secureData(referenceUser.getApplications(), userService.getSecretKey(), false);
                userService.setApplications(entriesDecrypted);
                return "redirect:/dashboard";
            }
        }
        model.addAttribute("error", "Wrong username or password");
        return "login";
    }

    @GetMapping("/logout")
    public String logout() throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {

        List<Application> encryptedApplications = cypher.secureData(userService.getApplications(), userService.getSecretKey(), true);
        userService.getAuthorisedUser().setApplications(encryptedApplications);

        userService.setSecretKey(null);
        userService.setAuthorisedUser(null);
        return "redirect:/";
    }
}
