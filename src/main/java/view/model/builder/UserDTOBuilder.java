package view.model.builder;

import model.Role;
import view.model.UserDTO;

import java.util.List;

public class UserDTOBuilder {
    private UserDTO userDTO;

    public UserDTOBuilder(){
        userDTO = new UserDTO();
    }

    public UserDTOBuilder setUsername(String username){
        userDTO.setUsername(username);
        return this;
    }

    public UserDTOBuilder setRoles(String roles){
        userDTO.setRoles(roles);
        return this;
    }

    public UserDTO build(){
        return userDTO;
    }
}
