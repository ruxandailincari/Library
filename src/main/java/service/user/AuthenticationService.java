package service.user;

import model.User;
import model.validator.Notification;

import java.util.List;

public interface AuthenticationService {
    Notification<Boolean> registerCustomer(String username, String password);
    Notification<User> login(String username, String password);
    boolean logout(User user);
    List<User> findAll();
    Notification<User> addEmployee(String username, String password);
    Notification<Boolean> deleteEmployee(String username);
}
