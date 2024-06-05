package loolu.loolu_backend.repositories;

import loolu.loolu_backend.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}