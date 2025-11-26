package mapper;

import model.Role;
import model.User;
import model.builder.UserBuilder;
import view.model.UserDTO;
import view.model.builder.UserDTOBuilder;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDTO convertUserToUserDTO(User user){
        StringBuilder stringBuilder = new StringBuilder();
        for(Role role : user.getRoles()){
            stringBuilder.append(role.getRole()).append(" ");
        }
        return new UserDTOBuilder()
                .setUsername(user.getUsername())
                .setRoles(stringBuilder.toString())
                .build();
    }

    public static User convertUserDTOToUser(UserDTO userDTO){
        return new UserBuilder()
                .setUsername(userDTO.getUsername())
                .build();
    }

    public static List<UserDTO> convertUserListToUserDTOList(List<User> users){
        return users.parallelStream().map(UserMapper::convertUserToUserDTO).collect(Collectors.toList());
    }

    public static List<User> convertUserDTOListToUserList(List<UserDTO> userDTOs){
        return userDTOs.parallelStream().map(UserMapper::convertUserDTOToUser).collect(Collectors.toList());
    }
}
