package loolu.loolu_backend.repositories;

import loolu.loolu_backend.models.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
}
