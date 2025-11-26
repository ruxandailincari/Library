package repository.book;

import model.Book;
import model.validator.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMock implements BookRepository{
    private final List<Book> books;

    public BookRepositoryMock(){
        books = new ArrayList<>();
    }

    @Override
    public List<Book> findAll() {
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return books.parallelStream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean save(Book book) {
        return books.add(book);
    }

    @Override
    public boolean delete(Book book) {
        return books.remove(book);
    }

    @Override
    public void removeAll() {
        books.clear();
    }

    @Override
    public Notification<Boolean> sellBooks(Book book, Integer nbOfBooks) {
        Optional<Book> bookToSell = books.parallelStream().filter(it -> it.getTitle().equals(book.getTitle()) && it.getAuthor().equals(book.getAuthor())).findFirst();
        Notification<Boolean> notification = new Notification<>();
        if(bookToSell.isEmpty()){
            notification.setResult(Boolean.FALSE);
            return notification;
        }

        Book foundBook = bookToSell.get();
        if(foundBook.getStock() < nbOfBooks){
            notification.setResult(Boolean.FALSE);
            return notification;
        }
        foundBook.setStock(foundBook.getStock() - nbOfBooks);
        notification.setResult(Boolean.TRUE);
        return notification;
    }
}
