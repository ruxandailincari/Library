package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import view.model.BookDTO;
import view.model.UserDTO;

import java.util.List;


public class AdminView {
    private Scene scene;
    private TableView bookTableView;
    private TableView usersTableView;
    private final ObservableList<BookDTO> booksObservableList;
    private final ObservableList<UserDTO> usersObservableList;
    private TextField usernameTextField;
    private PasswordField passwordField;
    private Button addEmployeeButton;
    private Button deleteEmployeeButton;
    private Text actionTarget;


    public AdminView(Stage primaryStage, List<BookDTO> books, List<UserDTO> users){
        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        this.scene = new Scene(gridPane, 720, 480);
        primaryStage.setScene(scene);

        booksObservableList = FXCollections.observableArrayList(books);
        usersObservableList = FXCollections.observableArrayList(users);
        initBookTableView(gridPane);
        initUserTableView(gridPane);
        initFields(gridPane);

        primaryStage.show();
    }

    private void initBookTableView(GridPane gridPane){
        bookTableView = new TableView<BookDTO>();
        bookTableView.setPlaceholder(new Label("No books to display"));

        TableColumn<BookDTO, String> titleColumn = new TableColumn<BookDTO, String>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<BookDTO, String> authorColumn = new TableColumn<BookDTO, String>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        TableColumn<BookDTO, String> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<BookDTO, String> stockColumn = new TableColumn<>("Stock");
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        bookTableView.getColumns().addAll(titleColumn, authorColumn, priceColumn, stockColumn);

        bookTableView.setItems(booksObservableList);

        gridPane.add(bookTableView, 0, 0, 3, 1);
    }

    private void initUserTableView(GridPane gridPane){
        usersTableView = new TableView<UserDTO>();
        usersTableView.setPlaceholder(new Label("No users to display"));

        TableColumn<UserDTO, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<UserDTO, String> rolesColumn = new TableColumn<>("Roles");
        rolesColumn.setCellValueFactory(new PropertyValueFactory<>("roles"));

        usersTableView.getColumns().addAll(usernameColumn, rolesColumn);

        usersTableView.setItems(usersObservableList);

        gridPane.add(usersTableView, 3, 0, 3, 1);
    }

    private void initializeGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initFields(GridPane gridPane){
        Label userName = new Label("User Name:");
        gridPane.add(userName, 0, 1);

        usernameTextField = new TextField();
        gridPane.add(usernameTextField, 1,1);

        Label password = new Label("Password:");
        gridPane.add(password, 2, 1);

        passwordField = new PasswordField();
        gridPane.add(passwordField, 3, 1);

        addEmployeeButton = new Button("Add Employee");
        gridPane.add(addEmployeeButton, 4, 1);

        deleteEmployeeButton = new Button("Delete Employee");
        gridPane.add(deleteEmployeeButton, 5, 1);

        actionTarget = new Text();
        actionTarget.setFill(Color.FIREBRICK);
        gridPane.add(actionTarget, 2,2, 2,1);
    }

    public Scene getScene(){
        return scene;
    }

    public void addAddEmployeeButtonListener(EventHandler<ActionEvent> addEmployeeButtonListener){
        addEmployeeButton.setOnAction(addEmployeeButtonListener);
    }

    public void addDeleteEmployeeButtonListener(EventHandler<ActionEvent> deleteEmployeeButtonListener){
        deleteEmployeeButton.setOnAction(deleteEmployeeButtonListener);
    }

    public String getUsername(){
        return usernameTextField.getText();
    }

    public String getPassword(){
        return passwordField.getText();
    }

    public void setActionTargetText(String text){
        this.actionTarget.setText(text);
    }

    public void addUserToObservableList(UserDTO userDTO){
        this.usersObservableList.add(userDTO);
    }

    public void deleteUserFromObservableList(UserDTO userDTO){
        this.usersObservableList.remove(userDTO);
    }

    public UserDTO getSelectedUser(){
        return (UserDTO) usersTableView.getSelectionModel().getSelectedItem();
    }


}
