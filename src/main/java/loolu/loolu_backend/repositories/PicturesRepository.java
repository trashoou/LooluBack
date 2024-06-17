package loolu.loolu_backend.repositories;

import loolu.loolu_backend.models.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PicturesRepository extends JpaRepository<Picture, Long> {
}
