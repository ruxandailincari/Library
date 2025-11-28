package launcher;

import controller.CustomerController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import mapper.BookMapper;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImpl;
import view.CustomerView;
import view.model.BookDTO;

import java.sql.Connection;
import java.util.List;

public class ComponentFactoryCustomer {
    private final CustomerView customerView;
    private final CustomerController customerController;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private static volatile ComponentFactoryCustomer instance;

    public static ComponentFactoryCustomer getInstance(Boolean componentsForTest, Stage primaryStage){
        if(instance == null){
            synchronized (ComponentFactoryCustomer.class){
                if(instance == null){
                    instance = new ComponentFactoryCustomer(componentsForTest, primaryStage);
                }
            }
        }
        return instance;
    }

    private ComponentFactoryCustomer(Boolean componentsForTest, Stage primaryStage){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();
        this.bookRepository = new BookRepositoryMySQL(connection);
        this.bookService = new BookServiceImpl(bookRepository);
        List<BookDTO> bookDTOS = BookMapper.convertBookListToBookDTOList(bookService.findAll());
        this.customerView = new CustomerView(primaryStage, bookDTOS);
        this.customerController = new CustomerController(customerView);
    }

    public CustomerView getCustomerView(){
        return customerView;
    }

    public CustomerController getCustomerController(){
        return customerController;
    }

    public BookService getBookService(){
        return bookService;
    }

    public BookRepository getBookRepository(){
        return bookRepository;
    }

}
