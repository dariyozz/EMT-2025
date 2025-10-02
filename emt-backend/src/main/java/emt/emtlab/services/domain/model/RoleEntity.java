package emt.emtlab.services.domain.model;

import emt.emtlab.services.domain.model.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "roles")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role roleName;

    public RoleEntity(Role roleName) {
        this.roleName = roleName;
    }
}