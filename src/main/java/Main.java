import database.DatabaseConnectionFactory;
import model.Book;
import model.Right;
import model.builder.BookBuilder;
import repository.book.BookRepository;
import repository.book.BookRepositoryCacheDecorator;
import repository.book.BookRepositoryMySQL;
import repository.book.Cache;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImpl;

import java.sql.Connection;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        BookRepository bookRepository = new BookRepositoryCacheDecorator(new BookRepositoryMySQL(DatabaseConnectionFactory.getConnectionWrapper(true).getConnection())
                , new Cache<>());

        BookService bookService = new BookServiceImpl(bookRepository);


        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(true).getConnection();
        RightsRolesRepository rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        UserRepository userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);

        AuthenticationService authenticationService = new AuthenticationServiceImpl(userRepository, rightsRolesRepository);

        if(userRepository.existsByUsername("Ruxanda")){
            System.out.println("Username already present into the user table");
        } else{
            authenticationService.register("Ruxanda", "parola123!");
        }

        System.out.println(authenticationService.login("Ruxanda", "parola123!"));
    }
}
