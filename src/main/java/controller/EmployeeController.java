package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mapper.BookMapper;
import model.Order;
import model.User;
import model.builder.OrderBuilder;
import model.validator.Notification;
import service.book.BookService;
import service.order.OrderService;
import view.EmployeeView;
import view.model.BookDTO;
import view.model.builder.BookDTOBuilder;

import java.time.LocalDateTime;

public class EmployeeController {

    private final EmployeeView employeeView;
    private final BookService bookService;
    private final User user;
    private final OrderService orderService;

    public EmployeeController(EmployeeView employeeView, BookService bookService, OrderService orderService, User user){
        this.employeeView = employeeView;
        this.bookService = bookService;
        this.orderService = orderService;
        this.user = user;

        this.employeeView.addSaveButtonListener(new SaveButtonListener());
        this.employeeView.addDeleteButtonListener(new DeleteButtonListener());
        this.employeeView.addSellButtonListener(new SellButtonListener());
    }

    private class SaveButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            String title = employeeView.getTitle();
            String author = employeeView.getAuthor();
            String price = employeeView.getPrice();
            String stock = employeeView.getStock();

            if(title.isEmpty() || author.isEmpty() || price.isEmpty() || stock.isEmpty()){
                employeeView.addDisplayAlertMessage("Save Error", "Problem at Author or Title fields", "Can not have an empty Title or Author field.");
            } else {
                BookDTO bookDTO = new BookDTOBuilder()
                        .setTitle(title)
                        .setAuthor(author)
                        .setPrice(Float.parseFloat(price))
                        .setStock(Integer.parseInt(stock))
                        .build();
                boolean savedBook = bookService.save(BookMapper.convertBookDTOToBook(bookDTO));

                if(savedBook){
                    employeeView.addDisplayAlertMessage("Save Successful", "Book added", "Book was successfully added to the database.");
                    employeeView.addBookToObservableList(bookDTO);
                } else {
                    employeeView.addDisplayAlertMessage("Save Error", "Problem at adding Book", "There was a problem at adding the book to the database. Please try again!");
                }
            }

        }
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent actionEvent) {
            BookDTO bookDTO = (BookDTO) employeeView.getBookTableView().getSelectionModel().getSelectedItem();
            if(bookDTO != null){
                boolean deletetionSuccessful = bookService.delete(BookMapper.convertBookDTOToBook(bookDTO));

                if(deletetionSuccessful){
                    employeeView.addDisplayAlertMessage("Delete Successful", "Book Deleted", "Book was successfully deleted from the database.");
                    employeeView.removeBookFromObservableList(bookDTO);
                } else {
                    employeeView.addDisplayAlertMessage("Delete Error", "Problem at deleting book", "There was a problem with the database. Please try again!");
                }
            } else {
                employeeView.addDisplayAlertMessage("Delete Error", "Problem at deleting book", "You must select a book before pressing the delete button.");
            }

        }
    }

    private class SellButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent actionEvent) {
            BookDTO bookDTO = (BookDTO) employeeView.getBookTableView().getSelectionModel().getSelectedItem();
            if(bookDTO != null){
                employeeView.initializeBookNumberStage();
                employeeView.addSellButton2Listener(new SellButton2Listener());

            } else {
                employeeView.addDisplayAlertMessage("Sell error", "Problem at selling book", "You must select a book before pressing the sell button");
            }
        }
    }

    private class SellButton2Listener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent actionEvent) {
            BookDTO bookDTO = (BookDTO) employeeView.getBookTableView().getSelectionModel().getSelectedItem();
            Integer nbOfBooksToSell = Integer.valueOf(employeeView.getNbOfBooksToSell());
            if(bookDTO != null){
               Notification<Boolean> sellSuccessful = bookService.sell(BookMapper.convertBookDTOToBook(bookDTO), nbOfBooksToSell);

                if (!sellSuccessful.hasErrors()){
                    employeeView.addDisplayAlertMessage("Sell successful", "Book sold", "The book stock was successfully updated in the database");
                    employeeView.removeBookFromObservableList(bookDTO);
                    if(bookDTO.getStock() - nbOfBooksToSell > 0) {
                        employeeView.addBookToObservableList(new BookDTOBuilder()
                                .setAuthor(bookDTO.getAuthor())
                                .setTitle(bookDTO.getTitle())
                                .setStock(bookDTO.getStock() - nbOfBooksToSell)
                                .setPrice(bookDTO.getPrice())
                                .build());
                    }

                    Order order = new OrderBuilder()
                            .setEmployeeId(user.getId())
                            .setBookNumber(nbOfBooksToSell)
                            .setTotalCost(orderService.calculateTotalCost(bookDTO.getPrice(), nbOfBooksToSell))
                            .setOrderDate(LocalDateTime.now())
                            .build();
                    orderService.add(order);

                } else {
                    employeeView.addDisplayAlertMessage("Sell Error", "Problem at selling book", sellSuccessful.getFormattedErrors());
                }

            } else {
                employeeView.addDisplayAlertMessage("Sell error", "Problem at selling book", "You must select a book before pressing the sell button");
            }
        }
    }

}
