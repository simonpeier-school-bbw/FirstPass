package net.simonpeier.firstpass.Services;

import net.simonpeier.firstpass.Model.User;

public interface UserService {
    User getUserByName(String name);
}
