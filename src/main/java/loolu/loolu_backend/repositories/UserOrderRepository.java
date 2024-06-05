package loolu.loolu_backend.repositories;

import loolu.loolu_backend.models.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOrderRepository extends JpaRepository<UserOrder, Long> {
}