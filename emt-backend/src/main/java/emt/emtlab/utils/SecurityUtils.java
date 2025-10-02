package emt.emtlab.utils;

import emt.emtlab.services.domain.model.User;
import emt.emtlab.services.domain.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
public class SecurityUtils {

    private final UserRepository userRepository;

    public SecurityUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public static Collection<SimpleGrantedAuthority> getAuthoritiesForRole(String role) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        switch (role) {
            case "ADMIN" -> {
                authorities.add(new SimpleGrantedAuthority("ADMIN"));
                authorities.add(new SimpleGrantedAuthority("LIBRARIAN"));
                authorities.add(new SimpleGrantedAuthority("USER"));
            }
            case "LIBRARIAN" -> {
                authorities.add(new SimpleGrantedAuthority("LIBRARIAN"));
                authorities.add(new SimpleGrantedAuthority("USER"));
            }
            case "USER" -> authorities.add(new SimpleGrantedAuthority("USER"));
        }

        return authorities;
    }
}