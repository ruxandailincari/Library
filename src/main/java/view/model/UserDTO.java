package view.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

import java.util.List;

public class UserDTO {
    private StringProperty username;

    public void setUsername(String username){
        usernameProperty().set(username);
    }

    public String getUsername(){
        return usernameProperty().get();
    }

    public StringProperty usernameProperty(){
        if(username == null){
            username = new SimpleStringProperty(this, "username");
        }
        return username;
    }

    private StringProperty roles;

    public void setRoles(String roles){
        rolesProperty().set(roles);
    }

    public String getRoles(){
        return rolesProperty().get();
    }

    public StringProperty rolesProperty(){
        if(roles == null){
            roles = new SimpleStringProperty(this, "roles");
        }
        return roles;
    }
}
