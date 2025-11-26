package launcher;

import controller.AdminController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import mapper.BookMapper;
import mapper.UserMapper;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImpl;
import view.AdminView;
import view.model.BookDTO;
import view.model.UserDTO;

import java.sql.Connection;
import java.util.List;

public class ComponentFactoryAdmin {
    private final AdminView adminView;
    private final AdminController adminController;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;
    private final AuthenticationService authenticationService;
    private static volatile ComponentFactoryAdmin instance;

    public static ComponentFactoryAdmin getInstance(Boolean componentsForTest, Stage stage){
        if(instance == null){
            synchronized (ComponentFactoryAdmin.class){
                if(instance == null){
                    instance = new ComponentFactoryAdmin(componentsForTest, stage);
                }
            }
        }
        return instance;
    }

    private ComponentFactoryAdmin(Boolean componentsForTest, Stage stage){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();
        this.bookRepository = new BookRepositoryMySQL(connection);
        this.bookService = new BookServiceImpl(bookRepository);
        List<BookDTO> bookDTOs = BookMapper.convertBookListToBookDTOList(bookService.findAll());

        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.authenticationService = new AuthenticationServiceImpl(userRepository, rightsRolesRepository);
        List<UserDTO> userDTOS = UserMapper.convertUserListToUserDTOList(authenticationService.findAll());
        this.adminView = new AdminView(stage, bookDTOs, userDTOS);
        this.adminController = new AdminController(adminView, authenticationService);

    }

    public AdminView getAdminView(){
        return adminView;
    }
}
