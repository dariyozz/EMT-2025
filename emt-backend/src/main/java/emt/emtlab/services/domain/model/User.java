package emt.emtlab.services.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import emt.emtlab.services.domain.model.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roleEntities;

    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;

    public User() {
    }

    public User(String username, String email, String password, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String username, String name, String surname, Set<Role> roles) {
        this.username = username;
        this.firstName = name;
        this.lastName = surname;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (roleEntities != null) {
            roleEntities.forEach(role ->
                    authorities.add(new SimpleGrantedAuthority(role.getRoleName().name())));
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void addRole(RoleEntity roleEntity) {
        if (roleEntities == null) {
            roleEntities = Set.of(roleEntity);
        } else {
            roleEntities.add(roleEntity);
        }
    }

    public String getHighestRole() {
        // ADMIN > LIBRARIAN > USER
        if (roleEntities == null || roleEntities.isEmpty()) {
            return Role.USER.name();
        }
        if (roleEntities.stream().anyMatch(role -> role.getRoleName() == Role.ADMIN)) {
            return Role.ADMIN.name();
        }
        if (roleEntities.stream().anyMatch(role -> role.getRoleName() == Role.LIBRARIAN)) {
            return Role.LIBRARIAN.name();
        }
        return Role.USER.name();
    }

    public void setAuthorities(Collection<SimpleGrantedAuthority> authorities) {
        this.roleEntities = authorities.stream()
                .map(authority -> new RoleEntity(Role.valueOf(authority.getAuthority())))
                .collect(Collectors.toSet());
    }
}