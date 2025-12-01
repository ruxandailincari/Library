package service.user;

import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import model.validator.UserValidator;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.List;

import static database.Constants.Roles.CUSTOMER;
import static database.Constants.Roles.EMPLOYEE;

public class AuthenticationServiceImpl implements AuthenticationService{
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;

    public AuthenticationServiceImpl(UserRepository userRepository, RightsRolesRepository rightsRolesRepository){
        this.userRepository = userRepository;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public Notification<Boolean> registerCustomer(String username, String password) {
        Notification<Boolean> userRegisterNotification = new Notification<>();
        boolean usernameNotUnique = userRepository.existsByUsername(username);
        if(usernameNotUnique){
            userRegisterNotification.addError("User with this email already exists in the database!");
            userRegisterNotification.setResult(Boolean.FALSE);
            return userRegisterNotification;
        }

        Role customerRole = rightsRolesRepository.findRoleByTitle(CUSTOMER);

        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setRoles(Collections.singletonList(customerRole))
                .build();

        UserValidator userValidator = new UserValidator(user);

        boolean userValid = userValidator.validate();
        if(!userValid){
            userValidator.getErrors().forEach(userRegisterNotification::addError);
            userRegisterNotification.setResult(Boolean.FALSE);
            return userRegisterNotification;
        } else {
            user.setPassword(hashPassword(password));
            Notification<Boolean> saveResult = userRepository.save(user);
            if(saveResult.hasErrors()){
                saveResult.getErrors().forEach(userRegisterNotification::addError);
                userRegisterNotification.setResult(Boolean.FALSE);
            } else {
                userRegisterNotification.setResult(Boolean.TRUE);
            }
        }

        return userRegisterNotification;
    }

    @Override
    public Notification<User> login(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, hashPassword(password));
    }

    @Override
    public boolean logout(User user) {
        return false;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Notification<User> addEmployee(String username, String password) {
        Notification<User> userRegisterNotification = new Notification<>();
        boolean usernameNotUnique = userRepository.existsByUsername(username);
        if(usernameNotUnique){
            userRegisterNotification.addError("User with this email already exists in the database!");
            return userRegisterNotification;
        }

        Role employeeRole = rightsRolesRepository.findRoleByTitle(EMPLOYEE);

        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setRoles(Collections.singletonList(employeeRole))
                .build();

        UserValidator userValidator = new UserValidator(user);

        boolean userValid = userValidator.validate();
        if(!userValid){
            userValidator.getErrors().forEach(userRegisterNotification::addError);
            return userRegisterNotification;
        } else {
            user.setPassword(hashPassword(password));
            Notification<Boolean> saveResult = userRepository.save(user);
            if(saveResult.hasErrors()){
                saveResult.getErrors().forEach(userRegisterNotification::addError);
            } else {
                userRegisterNotification.setResult(user);
            }
        }

        return userRegisterNotification;
    }

    @Override
    public Notification<Boolean> deleteEmployee(String username) {
        return userRepository.deleteEmployee(username);
    }

    private String hashPassword(String password){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for(byte b : hash){
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
