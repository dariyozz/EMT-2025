package emt.emtlab.services.domain.repository;

import emt.emtlab.services.domain.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsernameAndPassword(String username, String password);


    @EntityGraph(attributePaths = {"id", "username", "email", "firstName", "lastName"},
            type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT u FROM User u")
    List<User> findAllWithoutRoles();
}
