package emt.emtlab.services.domain.service.impl;

import emt.emtlab.exceptions.InvalidUsernameOrPasswordException;
import emt.emtlab.exceptions.PasswordsDoNotMatchException;
import emt.emtlab.exceptions.UsernameAlreadyExistsException;
import emt.emtlab.services.domain.model.RoleEntity;
import emt.emtlab.services.domain.model.User;
import emt.emtlab.services.domain.model.enums.Role;
import emt.emtlab.services.domain.repository.RoleRepository;
import emt.emtlab.services.domain.repository.UserRepository;
import emt.emtlab.services.domain.service.UserDomainService;
import emt.emtlab.utils.SecurityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDomainServiceImpl implements UserDomainService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    public UserDomainServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Get all authorities based on the user's highest role
        Collection<SimpleGrantedAuthority> authorities =
                SecurityUtils.getAuthoritiesForRole(user.getHighestRole());
        user.setAuthorities(authorities);
        return user;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                username));
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException(
                String.valueOf(userId)));
    }

    @Override
    public User register(
            String username, String email, String password, String repeatPassword, String firstName, String lastName, Set<Role> roles
    ) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty())
            throw new InvalidUsernameOrPasswordException();
        if (!password.equals(repeatPassword)) throw new PasswordsDoNotMatchException();
        if (userRepository.findByUsername(username).isPresent())
            throw new UsernameAlreadyExistsException(username);
        User user = new User(username, email, passwordEncoder.encode(password), firstName, lastName);

        // Convert Role enums to RoleEntity objects and set them to the user
        if (roles != null && !roles.isEmpty()) {
            Set<RoleEntity> roleEntities = roles.stream()
                    .map(role -> roleRepository.findByRoleName(role)
                            .orElseGet(() -> roleRepository.save(new RoleEntity(role))))
                    .collect(Collectors.toSet());
            user.setRoleEntities(roleEntities);
        } else {
            // Set default role if no roles provided
            RoleEntity defaultRole = roleRepository.findByRoleName(Role.USER)
                    .orElseGet(() -> roleRepository.save(new RoleEntity(Role.USER)));
            user.setRoleEntities(Set.of(defaultRole));
        }

        return userRepository.save(user);
    }

    @Override
    public User login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new InvalidUsernameOrPasswordException();
        }
        if (!passwordEncoder.matches(password, userRepository.findByUsername(username)
                .orElseThrow(InvalidUsernameOrPasswordException::new).getPassword())) {
            throw new InvalidUsernameOrPasswordException();
        }
        return userRepository.findByUsername(username).
                orElseThrow(InvalidUsernameOrPasswordException::new);
    }

}
