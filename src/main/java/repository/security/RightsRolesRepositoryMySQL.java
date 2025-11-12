package repository.security;

import model.Right;
import model.Role;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.*;

public class RightsRolesRepositoryMySQL implements RightsRolesRepository{
    private final Connection connection;

    public RightsRolesRepositoryMySQL(Connection connection){
        this.connection = connection;
    }

    @Override
    public void addRole(String role) {
        try{
            String sql = "INSERT IGNORE INTO " + ROLE + " values (null, ?);";
            PreparedStatement insertStatement = connection.prepareStatement(sql);
            insertStatement.setString(1, role);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addRight(String right) {
        try {
            String sql = "INSERT IGNORE INTO `" + RIGHT + "` values (null, ?);";
            PreparedStatement insertStatement = connection.prepareStatement(sql);
            insertStatement.setString(1, right);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Role findRoleByTitle(String role) {
        PreparedStatement preparedStatement;
        try {
            String fetchRoleSql = "Select * from " + ROLE + " where `role`=?;";
            preparedStatement = connection.prepareStatement(fetchRoleSql);
            preparedStatement.setString(1, role);
            ResultSet roleResultSet = preparedStatement.executeQuery();
            roleResultSet.next();
            Long roleId = roleResultSet.getLong("id");
            String roleTitle = roleResultSet.getString("role");
            return new Role(roleId, roleTitle, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Role findRoleById(Long roleId) {
        PreparedStatement preparedStatement;
        try {
            String fetchRoleSql = "Select * from " + ROLE + " where `id`=?";
            preparedStatement = connection.prepareStatement(fetchRoleSql);
            preparedStatement.setLong(1, roleId);
            ResultSet roleResultSet = preparedStatement.executeQuery();
            roleResultSet.next();
            String roleTitle = roleResultSet.getString("role");
            return new Role(roleId, roleTitle, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Right findRightByTitle(String title) {
        PreparedStatement preparedStatement;
        try {
            String fetchRightSql = "Select * from `" + RIGHT  + "` where `right`=?;";
            preparedStatement = connection.prepareStatement(fetchRightSql);
            preparedStatement.setString(1, title);
            ResultSet roleResultSet = preparedStatement.executeQuery();
            roleResultSet.next();
            Long rightId = roleResultSet.getLong("id");
            String rightTitle = roleResultSet.getString("right");
            return new Right(rightId, rightTitle);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addRolesToUser(User user, List<Role> roles) {
        PreparedStatement preparedStatement;
        try {
            for(Role role : roles){
                String sql = "INSERT INTO `user_role` values (null, ?, ?);";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setLong(1, user.getId());
                preparedStatement.setLong(2, role.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Role> findRolesForUser(Long userId) {
        try {
            List<Role> roles = new ArrayList<>();
            String fetchRoleSql = "Select * from " + USER_ROLE + " where `user_id`=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(fetchRoleSql);
            preparedStatement.setLong(1, userId);
            ResultSet userRoleResultSet = preparedStatement.executeQuery();
            while(userRoleResultSet.next()){
                long roleId = userRoleResultSet.getLong("role_id");
                roles.add(findRoleById(roleId));
            }
            return roles;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addRoleRight(Long roleId, Long rightId) {
        try {
            String sql = "INSERT IGNORE INTO " + ROLE_RIGHT + " values (null, ?, ?);";
            PreparedStatement insertStatement = connection.prepareStatement(sql);
            insertStatement.setLong(1, roleId);
            insertStatement.setLong(2, rightId);
            insertStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
