package loolu.loolu_backend.services;

import loolu.loolu_backend.models.Category;
import loolu.loolu_backend.models.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Long id);

    Category getCategoryById(Long categoryId);

    Product saveProduct(Product product);
    void deleteProduct(Long id);
    List<Product> filterProducts(String title, Double price, Double price_min, Double price_max, Long categoryId);
//    List<Product> findProductsByTitle(String title);
//    List<Product> findProductsByCategory(Long categoryId);
//    List<Product> findProductsByPriceBetween(Double minPrice, Double maxPrice);
//    List<Product> findProductsByTitleAndPriceBetween(String title, Double minPrice, Double maxPrice);
}
