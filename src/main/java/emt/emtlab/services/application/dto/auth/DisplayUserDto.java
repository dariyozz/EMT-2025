package emt.emtlab.services.application.dto.auth;


import emt.emtlab.services.domain.model.RoleEntity;
import emt.emtlab.services.domain.model.User;
import emt.emtlab.services.domain.model.enums.Role;

import java.util.Set;
import java.util.stream.Collectors;

public record DisplayUserDto(String username, String name, String surname, Set<Role> roles) {

    public static DisplayUserDto from(User user) {
        return new DisplayUserDto(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getRoleEntities()
                        .stream()
                        .map(RoleEntity::getRoleName)
                        .collect(Collectors.toSet())
        );
    }

    public User toUser() {
        return new User(username, name, surname, roles);
    }
}
