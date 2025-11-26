package service.book;

import model.Book;
import model.validator.Notification;

import java.util.List;

public interface BookService {
    List<Book> findAll();
    Book findById(Long id);
    boolean save(Book book);
    boolean delete(Book book);
    int getAgeOfBook(Long id);
    public Notification<Boolean> sell(Book book, Integer nbOfBooks);
}
