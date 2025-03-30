package emt.emtlab.services.domain.service.impl;

import emt.emtlab.exceptions.InvalidUsernameOrPasswordException;
import emt.emtlab.exceptions.PasswordsDoNotMatchException;
import emt.emtlab.exceptions.UsernameAlreadyExistsException;
import emt.emtlab.services.domain.model.User;
import emt.emtlab.services.domain.model.enums.Role;
import emt.emtlab.services.domain.repository.UserRepository;
import emt.emtlab.services.domain.service.UserDomainService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserDomainServiceImpl implements UserDomainService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDomainServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                username));
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
            String username, String email, String password, String repeatPassword, String firstName, String lastName, Set<Role> role
    ) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty())
            throw new InvalidUsernameOrPasswordException();
        if (!password.equals(repeatPassword)) throw new PasswordsDoNotMatchException();
        if (userRepository.findByUsername(username).isPresent())
            throw new UsernameAlreadyExistsException(username);
        User user = new User(username, email,passwordEncoder.encode(password), firstName, lastName, role);
        return userRepository.save(user);
    }

    @Override
    public User login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new InvalidUsernameOrPasswordException();
        }
        return userRepository.findByUsernameAndPassword(username, password).orElseThrow(
                InvalidUsernameOrPasswordException::new);
    }

}
