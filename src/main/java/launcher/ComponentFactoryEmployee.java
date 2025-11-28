package launcher;

import controller.EmployeeController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import mapper.BookMapper;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImpl;
import view.EmployeeView;
import view.model.BookDTO;

import java.sql.Connection;
import java.util.List;

public class ComponentFactoryEmployee {

    private final EmployeeView employeeView;
    private final EmployeeController employeeController;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private static volatile ComponentFactoryEmployee instance;

    public static ComponentFactoryEmployee getInstance(Boolean componentsForTest, Stage primaryStage){
        if (instance == null){
            synchronized (ComponentFactoryEmployee.class) {
                if (instance == null) {
                    instance = new ComponentFactoryEmployee(componentsForTest, primaryStage);
                }
            }
        }
        return  instance;
    }

    private ComponentFactoryEmployee(Boolean componentsForTest, Stage primaryStage){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();
        this.bookRepository = new BookRepositoryMySQL(connection);
        this.bookService = new BookServiceImpl(bookRepository);
        List<BookDTO> bookDTOs = BookMapper.convertBookListToBookDTOList(bookService.findAll());
        this.employeeView = new EmployeeView(primaryStage, bookDTOs);
        this.employeeController = new EmployeeController(employeeView, bookService);

    }

    public EmployeeView getBookView() {
        return employeeView;
    }

    public EmployeeController getBookController() {
        return employeeController;
    }

    public BookRepository getBookRepository() {
        return bookRepository;
    }

    public BookService getBookService() {
        return bookService;
    }
}
