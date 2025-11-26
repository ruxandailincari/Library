package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import launcher.ComponentFactoryAdmin;
import launcher.ComponentFactoryBook;
import model.Role;
import model.User;
import model.validator.Notification;
import service.user.AuthenticationService;
import view.AdminView;
import view.BookView;
import view.LoginView;

import java.util.List;

import static database.Constants.Roles.ADMINISTRATOR;
import static database.Constants.Roles.CUSTOMER;


public class LoginController {
    private final LoginView loginView;
    private final AuthenticationService authenticationService;

    public LoginController(LoginView loginView, AuthenticationService authenticationService){
        this.loginView = loginView;
        this.authenticationService = authenticationService;

        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());
    }

    private class LoginButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent actionEvent) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<User> loginNotification = authenticationService.login(username, password);

            if(loginNotification.hasErrors()){
                loginView.setActionTargetText(loginNotification.getFormattedErrors());
            } else {
                String userChoice = loginView.getUserChoice();
                List<Role> userRoles = loginNotification.getResult().getRoles();

                if(userChoice.equals(CUSTOMER) && userRoles.stream().anyMatch(x -> x.getRole().equals(CUSTOMER))) {
                    ComponentFactoryBook componentFactoryBook = ComponentFactoryBook.getInstance(false, loginView.getStage());
                    BookView bookView = componentFactoryBook.getBookView();

                    loginView.getStage().setScene(bookView.getScene());
                }

                if(userChoice.equals(ADMINISTRATOR) && userRoles.stream().anyMatch(x -> x.getRole().equals(ADMINISTRATOR))){
                    ComponentFactoryAdmin componentFactoryAdmin = ComponentFactoryAdmin.getInstance(false, loginView.getStage());
                    AdminView adminView = componentFactoryAdmin.getAdminView();

                    loginView.getStage().setScene(adminView.getScene());
                }
            }
        }
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent actionEvent) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<Boolean> registerNotification = authenticationService.registerCustomer(username, password);

            if(registerNotification.hasErrors()){
                loginView.setActionTargetText(registerNotification.getFormattedErrors());
            } else {
                loginView.setActionTargetText("Register successful!");
            }
        }
    }
}
