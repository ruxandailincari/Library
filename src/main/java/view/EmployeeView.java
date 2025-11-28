package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

import view.model.BookDTO;

import java.util.List;

public class EmployeeView {
    private TableView bookTableView;
    private final ObservableList<BookDTO> booksObservableList;
    private TextField authorTextField;
    private TextField titleTextField;
    private TextField priceTextField;
    private TextField stockTextField;
    private Label authorLabel;
    private Label titleLabel;
    private Label priceLabel;
    private Label stockLabel;
    private Button saveButton;
    private Button deleteButton;
    private Button sellButton;
    private Scene scene;
    private Stage selectBookNumberStage;
    private Scene selectBookNumberScene;
    private TextField numberOfBooksTextField;
    private Button sellButton2;

    public EmployeeView(Stage primaryStage, List<BookDTO> books){
        primaryStage.setTitle("Library");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        this.scene = new Scene(gridPane, 720, 480);
        primaryStage.setScene(scene);

        booksObservableList = FXCollections.observableArrayList(books);
        initTableView(gridPane);

        initSaveOptions(gridPane);

        primaryStage.show();
    }

    private void initializeGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initTableView(GridPane gridPane){
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

        gridPane.add(bookTableView, 0, 0, 5, 1);
    }

    private void initSaveOptions(GridPane gridPane){
        titleLabel = new Label("Title");
        gridPane.add(titleLabel, 1, 1);

        titleTextField = new TextField();
        gridPane.add(titleTextField, 2, 1);

        authorLabel = new Label("Author");
        gridPane.add(authorLabel, 3, 1);

        authorTextField = new TextField();
        gridPane.add(authorTextField, 4, 1);

        priceLabel = new Label("Price");
        gridPane.add(priceLabel, 1, 2);

        priceTextField = new TextField();
        gridPane.add(priceTextField, 2,2);

        stockLabel = new Label("Stock");
        gridPane.add(stockLabel, 3, 2);

        stockTextField = new TextField();
        gridPane.add(stockTextField, 4, 2);

        saveButton = new Button("Save");
        gridPane.add(saveButton, 5, 1);

        deleteButton = new Button("Delete");
        gridPane.add(deleteButton, 6, 1);

        sellButton = new Button("Sell");
        gridPane.add(sellButton, 7, 1);
    }

    public void addSaveButtonListener(EventHandler<ActionEvent> saveButtonListener){
        saveButton.setOnAction(saveButtonListener);
    }

    public void addDeleteButtonListener(EventHandler<ActionEvent> deleteButtonListener){
        deleteButton.setOnAction(deleteButtonListener);
    }

    public void addSellButtonListener(EventHandler<ActionEvent> sellButtonListener){
        sellButton.setOnAction(sellButtonListener);
    }

    public void addDisplayAlertMessage(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public void initializeBookNumberStage(){
        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        this.selectBookNumberStage = new Stage();
        this.selectBookNumberScene = new Scene(gridPane, 300, 300);
        selectBookNumberStage.setScene(selectBookNumberScene);

        initializeStage2Fields(gridPane);

        selectBookNumberStage.show();
    }

    public void initializeStage2Fields(GridPane gridPane){
        Label label = new Label("Number of books to sell:");
        gridPane.add(label, 0, 0);

        this.numberOfBooksTextField = new TextField();
        gridPane.add(numberOfBooksTextField, 0, 1);

        this.sellButton2 = new Button("Sell");
        HBox sellButton2HBox = new HBox(10);
        sellButton2HBox.setAlignment(Pos.CENTER);
        sellButton2HBox.getChildren().add(sellButton2);
        gridPane.add(sellButton2HBox, 0, 2);
    }

    public void addSellButton2Listener(EventHandler<ActionEvent> sellButton2Listener){
        this.sellButton2.setOnAction(sellButton2Listener);
    }

    public String getTitle(){
        return titleTextField.getText();
    }

    public String getAuthor(){
        return authorTextField.getText();
    }

    public String getPrice(){ return priceTextField.getText();}

    public String getStock(){ return stockTextField.getText();}

    public String getNbOfBooksToSell(){return numberOfBooksTextField.getText();}

    public void addBookToObservableList(BookDTO bookDTO){
        this.booksObservableList.add(bookDTO);
    }

    public void removeBookFromObservableList(BookDTO bookDTO){
        this.booksObservableList.remove(bookDTO);
    }

    public TableView getBookTableView(){
        return bookTableView;
    }

    public Scene getScene(){
        return scene;
    }
}
