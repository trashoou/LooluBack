package loolu.loolu_backend.repositories;

import loolu.loolu_backend.domain.User;
import loolu.loolu_backend.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}