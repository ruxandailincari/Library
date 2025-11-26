package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mapper.BookMapper;
import model.validator.Notification;
import service.book.BookService;
import view.BookView;
import view.model.BookDTO;
import view.model.builder.BookDTOBuilder;

public class BookController {

    private final BookView bookView;
    private final BookService bookService;

    public BookController(BookView bookView, BookService bookService){
        this.bookView = bookView;
        this.bookService = bookService;

        this.bookView.addSaveButtonListener(new SaveButtonListener());
        this.bookView.addDeleteButtonListener(new DeleteButtonListener());
        this.bookView.addSellButtonListener(new SellButtonListener());
    }

    private class SaveButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            String title = bookView.getTitle();
            String author = bookView.getAuthor();
            String price = bookView.getPrice();
            String stock = bookView.getStock();

            if(title.isEmpty() || author.isEmpty() || price.isEmpty() || stock.isEmpty()){
                bookView.addDisplayAlertMessage("Save Error", "Problem at Author or Title fields", "Can not have an empty Title or Author field.");
            } else {
                BookDTO bookDTO = new BookDTOBuilder()
                        .setTitle(title)
                        .setAuthor(author)
                        .setPrice(Float.parseFloat(price))
                        .setStock(Integer.parseInt(stock))
                        .build();
                boolean savedBook = bookService.save(BookMapper.convertBookDTOToBook(bookDTO));

                if(savedBook){
                    bookView.addDisplayAlertMessage("Save Successful", "Book added", "Book was successfully added to the database.");
                    bookView.addBookToObservableList(bookDTO);
                } else {
                    bookView.addDisplayAlertMessage("Save Error", "Problem at adding Book", "There was a problem at adding the book to the database. Please try again!");
                }
            }

        }
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent actionEvent) {
            BookDTO bookDTO = (BookDTO) bookView.getBookTableView().getSelectionModel().getSelectedItem();
            if(bookDTO != null){
                boolean deletetionSuccessful = bookService.delete(BookMapper.convertBookDTOToBook(bookDTO));

                if(deletetionSuccessful){
                    bookView.addDisplayAlertMessage("Delete Successful", "Book Deleted", "Book was successfully deleted from the database.");
                    bookView.removeBookFromObservableList(bookDTO);
                } else {
                    bookView.addDisplayAlertMessage("Delete Error", "Problem at deleting book", "There was a problem with the database. Please try again!");
                }
            } else {
                bookView.addDisplayAlertMessage("Delete Error", "Problem at deleting book", "You must select a book before pressing the delete button.");
            }

        }
    }

    private class SellButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent actionEvent) {
            BookDTO bookDTO = (BookDTO) bookView.getBookTableView().getSelectionModel().getSelectedItem();
            if(bookDTO != null){
                bookView.initializeBookNumberStage();
                bookView.addSellButton2Listener(new SellButton2Listener());

            } else {
                bookView.addDisplayAlertMessage("Sell error", "Problem at selling book", "You must select a book before pressing the sell button");
            }
        }
    }

    private class SellButton2Listener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent actionEvent) {
            BookDTO bookDTO = (BookDTO) bookView.getBookTableView().getSelectionModel().getSelectedItem();
            Integer nbOfBooksToSell = Integer.valueOf(bookView.getNbOfBooksToSell());
            if(bookDTO != null){
               Notification<Boolean> sellSuccessful = bookService.sell(BookMapper.convertBookDTOToBook(bookDTO), nbOfBooksToSell);

                if (!sellSuccessful.hasErrors()){
                    bookView.addDisplayAlertMessage("Sell successful", "Book sold", "The book stock was successfully updated in the database");
                    bookView.removeBookFromObservableList(bookDTO);
                    if(bookDTO.getStock() - nbOfBooksToSell > 0) {
                        bookView.addBookToObservableList(new BookDTOBuilder()
                                .setAuthor(bookDTO.getAuthor())
                                .setTitle(bookDTO.getTitle())
                                .setStock(bookDTO.getStock() - nbOfBooksToSell)
                                .setPrice(bookDTO.getPrice())
                                .build());
                    }
                } else {
                    bookView.addDisplayAlertMessage("Sell Error", "Problem at selling book", sellSuccessful.getFormattedErrors());
                }

            } else {
                bookView.addDisplayAlertMessage("Sell error", "Problem at selling book", "You must select a book before pressing the sell button");
            }
        }
    }

}
