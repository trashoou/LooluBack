package loolu.loolu_backend.services;

import loolu.loolu_backend.models.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Long id);
    Product saveProduct(Product product);
    void deleteProduct(Long id);

    List<Product> findProductsByTitle(String title);
}
