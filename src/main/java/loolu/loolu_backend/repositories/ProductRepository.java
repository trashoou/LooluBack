package loolu.loolu_backend.repositories;

import loolu.loolu_backend.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByTitleContainingIgnoreCase(String title);
    List<Product> findByCategory_Id(Long categoryId);
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
    List<Product> findProductsByTitleAndPriceBetween(String title, Double minPrice, Double maxPrice);
}
