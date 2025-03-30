package emt.emtlab.services.domain.service;

import emt.emtlab.services.domain.model.User;
import emt.emtlab.services.domain.model.enums.Role;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Set;

public interface UserDomainService extends UserDetailsService {

    User register(String username, String email, String password,String repeatedPass, String firstName, String lastName, Set<Role> role);

    User login(String username, String password);

    User findByUsername(String username);


    User findById(Long userId);
}
