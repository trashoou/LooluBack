package loolu.loolu_backend.services.impl;

import loolu.loolu_backend.models.Category;
import loolu.loolu_backend.models.Product;
import loolu.loolu_backend.repositories.CategoryRepository;
import loolu.loolu_backend.repositories.PicturesRepository;
import loolu.loolu_backend.repositories.ProductRepository;
import loolu.loolu_backend.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final PicturesRepository picturesRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, PicturesRepository picturesRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.picturesRepository = picturesRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    @Override
    public Product getProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        } else {
            throw new IllegalArgumentException("Product with ID " + id + " not found");
        }
    }
    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    @Override
    public Product saveProduct(Product product) {
        Category category = getCategoryById(product.getCategory().getId());
        if (category == null) {
            throw new IllegalArgumentException("Category with ID " + product.getCategory().getId() + " not found");
        }

        product.setCategory(category);

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        Product existingProduct = getProductById(id);
        if (existingProduct == null) {
            throw new IllegalArgumentException("Product with ID " + id + " not found");
        }
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> filterProducts(String title, Double price, Double price_min, Double price_max, Long categoryId) {
        List<Product> products = productRepository.findAll();

        // Фильтрация по названию
        if (title != null) {
            products = products.stream()
                    .filter(product -> product.getTitle().toLowerCase().contains(title.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Фильтрация по цене
        if (price != null) {
            products = products.stream()
                    .filter(product -> product.getPrice() == price)
                    .collect(Collectors.toList());
        }

        // Фильтрация по диапазону цен
        if (price_min != null && price_max != null) {
            products = products.stream()
                    .filter(product -> product.getPrice() >= price_min && product.getPrice() <= price_max)
                    .collect(Collectors.toList());
        }

        // Фильтрация по категории
        if (categoryId != null) {
            products = products.stream()
                    .filter(product -> product.getCategory().getId() == categoryId)
                    .collect(Collectors.toList());
        }

        return products;
    }


}
