package repository.security;

import model.Right;
import model.Role;
import model.User;

import java.util.List;

public interface RightsRolesRepository {
    void addRole(String role);
    void addRight(String right);
    Role findRoleByTitle(String title);
    Role findRoleById(Long roleId);
    Right findRightByTitle(String title);
    void addRolesToUser(User user, List<Role> roles);
    List<Role> findRolesForUser(Long userId);
    void addRoleRight(Long roleId, Long rightId);

}
