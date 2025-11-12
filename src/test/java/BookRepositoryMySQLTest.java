import database.DatabaseConnectionFactory;
import model.Book;
import model.builder.BookBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.book.BookRepositoryMySQL;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookRepositoryMySQLTest {
    private static BookRepositoryMySQL bookRepositoryMySQL;

    @BeforeAll
    public static void setup(){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(true).getConnection();
        bookRepositoryMySQL = new BookRepositoryMySQL(connection);
    }

    @BeforeEach
    public void deleteData(){
        bookRepositoryMySQL.removeAll();
    }

    @Test
    public void findAll(){
        List<Book> books = bookRepositoryMySQL.findAll();
        assertEquals(0, books.size());
    }

    @Test
    public void save(){
        assertTrue(bookRepositoryMySQL.save(new BookBuilder().setTitle("Emma").setAuthor("Jane Austen").setPublishedDate(LocalDate.of(1808,1,1)).build()));
    }

    @Test
    public void delete(){
        Book book = new BookBuilder().setTitle("Persuasion").setAuthor("Jane Austen").setPublishedDate(LocalDate.of(1808,1,1)).build();
        bookRepositoryMySQL.save(book);
        assertTrue(bookRepositoryMySQL.delete(book));
    }

    @Test
    public void findById(){
        Book book = new BookBuilder().setTitle("Pride and Prejudice").setAuthor("Jane Austen").setPublishedDate(LocalDate.of(1808,1,1)).build();
        bookRepositoryMySQL.save(book);
        List<Book> books = bookRepositoryMySQL.findAll();
        final Optional<Book> foundBook = bookRepositoryMySQL.findById(books.getFirst().getId());
        assertTrue(foundBook.isPresent());
    }
}
