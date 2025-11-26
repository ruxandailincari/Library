package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mapper.UserMapper;
import model.User;
import model.validator.Notification;
import service.user.AuthenticationService;
import view.AdminView;
import view.model.UserDTO;


public class AdminController {
    private final AdminView adminView;
    private final AuthenticationService authenticationService;

    public AdminController(AdminView adminView, AuthenticationService authenticationService){
        this.adminView = adminView;
        this.authenticationService = authenticationService;

        this.adminView.addAddEmployeeButtonListener(new AddEmployeeButtonListener());
        this.adminView.addDeleteEmployeeButtonListener(new DeleteEmployeeButtonListener());
    }

    private class AddEmployeeButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent actionEvent) {
            String username = adminView.getUsername();
            String password = adminView.getPassword();

            Notification<User> registerEmployeeNotification = authenticationService.addEmployee(username, password);

            if(registerEmployeeNotification.hasErrors()){
                adminView.setActionTargetText(registerEmployeeNotification.getFormattedErrors());
            } else {
                adminView.setActionTargetText("Employee Register successful!");
                UserDTO userDTO = UserMapper.convertUserToUserDTO(registerEmployeeNotification.getResult());
                adminView.addUserToObservableList(userDTO);
            }
        }
    }

    private class DeleteEmployeeButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent actionEvent) {
            UserDTO userDTO = adminView.getSelectedUser();
            String username = userDTO.getUsername();

            Notification<Boolean> deleteEmployeeNotification = authenticationService.deleteEmployee(username);

            if(deleteEmployeeNotification.hasErrors()){
                adminView.setActionTargetText(deleteEmployeeNotification.getFormattedErrors());
            } else {
                adminView.setActionTargetText("Employee deletion successful!");
                adminView.deleteUserFromObservableList(userDTO);
            }
        }
    }
}
