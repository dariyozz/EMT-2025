package emt.emtlab.services.domain.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class LoggedInUserLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    private String JWT_token;

    private LocalDateTime loginTime;

    private Long tokenExpirationTime;

    public LoggedInUserLogs() {
    }

    public LoggedInUserLogs(User user, String token, Long expirationTime) {
        this.user = user;
        this.JWT_token = token;
        this.loginTime = LocalDateTime.now();
        this.tokenExpirationTime = expirationTime;
    }
}
