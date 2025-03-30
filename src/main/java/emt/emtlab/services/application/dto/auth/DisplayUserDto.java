package emt.emtlab.services.application.dto.auth;


import emt.emtlab.services.domain.model.User;
import emt.emtlab.services.domain.model.enums.Role;

import java.util.Set;

public record DisplayUserDto(String username, String name, String surname, Set<Role> roles) {

    public static DisplayUserDto from(User user) {
        return new DisplayUserDto(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole()
        );
    }

    public User toUser() {
        return new User(username, name, surname, roles);
    }
}
