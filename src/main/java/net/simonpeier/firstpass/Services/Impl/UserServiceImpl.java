package net.simonpeier.firstpass.Services.Impl;

import net.simonpeier.firstpass.Model.User;
import net.simonpeier.firstpass.Services.UserService;
import org.springframework.stereotype.Service;

import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private List<User> users = new ArrayList(Arrays.asList(new User("Philipp", "asdf", new ArrayList<>())));
    @Override
    public User getUserByName(String name) {
        for (User user : users) {
            if (name == user.getUsername()) {
                return user;
            }
        }
        return null;
    }
}
