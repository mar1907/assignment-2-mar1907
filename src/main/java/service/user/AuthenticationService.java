package service.user;

import model.User;
import model.validation.Notification;

public interface AuthenticationService {

    Notification<Boolean> register(String username, String password, Integer admin);

    Notification<User> login(String username, String password);

    String encodePassword(String password);
}
