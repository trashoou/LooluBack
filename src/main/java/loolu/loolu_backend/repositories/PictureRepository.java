package loolu.loolu_backend.repositories;

import loolu.loolu_backend.models.Picture;
import loolu.loolu_backend.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface PictureRepository extends JpaRepository<Picture, Long> {
    Set<Picture> findByProduct(Product product);
}
