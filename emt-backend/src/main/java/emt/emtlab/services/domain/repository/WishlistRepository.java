package emt.emtlab.services.domain.repository;

import emt.emtlab.services.domain.model.User;
import emt.emtlab.services.domain.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Optional<Wishlist> findByUser(User user);
}