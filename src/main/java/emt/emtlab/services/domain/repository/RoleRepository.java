package emt.emtlab.services.domain.repository;

import emt.emtlab.services.domain.model.RoleEntity;
import emt.emtlab.services.domain.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByRoleName(Role role);
}
