package repository.user;

import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import repository.security.RightsRolesRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Roles.EMPLOYEE;
import static database.Constants.Tables.USER;
import static database.Constants.Tables.USER_ROLE;

public class UserRepositoryMySQL implements UserRepository{

    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;

    public UserRepositoryMySQL(Connection connection, RightsRolesRepository rightsRolesRepository){
        this.connection = connection;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public List<User> findAll() {
        String sql = "Select * from `" + USER + "` ;";

        List<User> users = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                users.add(getUserFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException{
        return new UserBuilder()
                .setId(resultSet.getLong("id"))
                .setUsername(resultSet.getString("username"))
                .setPassword(resultSet.getString("password"))
                .setRoles(rightsRolesRepository.findRolesForUser(resultSet.getLong("id")))
                .build();
    }

    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) {
        Notification<User> findByUsernameAndPasswordNotification = new Notification<>();
        try {
            String fetchUserSql = "Select * from `" + USER + "` where username=? and password=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(fetchUserSql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet userResultSet = preparedStatement.executeQuery();
            if (userResultSet.next()){

                User user = new UserBuilder()
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password"))
                        .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
                        .build();
                findByUsernameAndPasswordNotification.setResult(user);
            } else {
                findByUsernameAndPasswordNotification.addError("Invalid username or password!");
                return findByUsernameAndPasswordNotification;
            }
        } catch (SQLException e){
            System.out.println(e.toString());
            findByUsernameAndPasswordNotification.addError("Something is wrong with the Database!");

        }
        return findByUsernameAndPasswordNotification;
    }

    @Override
    public Notification<Boolean> save(User user) {
        Notification<Boolean> saveNotification = new Notification<>();
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("Insert INTO `" + USER + "` values (null, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setString(1, user.getUsername());
            insertUserStatement.setString(2, user.getPassword());
            insertUserStatement.executeUpdate();

            ResultSet rs = insertUserStatement.getGeneratedKeys();
            rs.next();
            long userId = rs.getLong(1);
            user.setId(userId);

            rightsRolesRepository.addRolesToUser(user, user.getRoles());
            saveNotification.setResult(Boolean.TRUE);
            return saveNotification;
        } catch (SQLException e){
            System.out.println(e.toString());
            saveNotification.addError("Something is wrong with the Database!");
            saveNotification.setResult(Boolean.FALSE);
            return saveNotification;
        }
    }

    @Override
    public void removeAll() {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from user where id>=0;";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean existsByUsername(String email) {
        try {
            String fetchUserSql = "Select * from `" + USER + "` where `username`=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(fetchUserSql);
            preparedStatement.setString(1, email);
            ResultSet userResultSet = preparedStatement.executeQuery();
            return userResultSet.next();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Notification<Boolean> deleteEmployee(String email) {
        Notification<Boolean> deleteNotification = new Notification<>();

        try{
            Long user_id = getUserIdForUsername(email);
            if(user_id == null) {
                deleteNotification.addError("User with this email doesn't exist in the database!");
                deleteNotification.setResult(Boolean.FALSE);
                return deleteNotification;
            }

            Role role = rightsRolesRepository.findRoleByTitle(EMPLOYEE);
            Long roleId = role.getId();
            String deleteEmployeeRole = "Delete from `" + USER_ROLE + "` where user_id=? and role_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteEmployeeRole);
            preparedStatement.setLong(1, user_id);
            preparedStatement.setLong(2, roleId);
            preparedStatement.executeUpdate();

            String remainingRoles = "Select * from `" + USER_ROLE + "` where user_id=?;";
            PreparedStatement preparedStatement1 = connection.prepareStatement(remainingRoles);
            preparedStatement1.setLong(1, user_id);
            ResultSet resultSet = preparedStatement1.executeQuery();
            if(!resultSet.next()){
                String deleteEmployeeFromUser = "Delete from `" + USER + "` where username=?;";
                PreparedStatement preparedStatement2 = connection.prepareStatement(deleteEmployeeFromUser);
                preparedStatement2.setString(1, email);
                preparedStatement2.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            deleteNotification.addError("Something is wrong with the database!");
            deleteNotification.setResult(Boolean.FALSE);
            return deleteNotification;
        }

        deleteNotification.setResult(Boolean.TRUE);
        return deleteNotification;
    }

    private Long getUserIdForUsername(String email) throws SQLException{
        String fetchUserSql = "Select * from `" + USER + "` where username=?;";
        PreparedStatement preparedStatement = connection.prepareStatement(fetchUserSql);
        preparedStatement.setString(1, email);
        ResultSet userResultSet = preparedStatement.executeQuery();
        if(userResultSet.next()){
            return userResultSet.getLong("id");
        }
        return null;
    }
}
