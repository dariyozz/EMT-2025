package emt.emtlab.services.domain.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, LIBRARIAN, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
