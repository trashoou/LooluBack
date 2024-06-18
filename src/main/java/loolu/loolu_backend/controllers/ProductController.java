package loolu.loolu_backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import loolu.loolu_backend.dto.ProductDTO;
import loolu.loolu_backend.dto.ProductRequest;
import loolu.loolu_backend.models.Picture;
import loolu.loolu_backend.models.Product;
import loolu.loolu_backend.repositories.PictureRepository;
import loolu.loolu_backend.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Tag(name = "Product Controller", description = "Controller for managing products")
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    private final ProductService productService;
    private final PictureRepository pictureRepository;

    @Autowired
    public ProductController(ProductService productService, PictureRepository pictureRepository) {
        this.productService = productService;
        this.pictureRepository = pictureRepository;
    }

    @Operation(
            summary = "Get all products",
            description = "Retrieve all products available in the database"
    )
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getFilteredProducts(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Double price_min,
            @RequestParam(required = false) Double price_max,
            @RequestParam(required = false) Long categoryId) {

        List<Product> filteredProducts = productService.filterProducts(title, price, price_min, price_max, categoryId);

        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Product product : filteredProducts) {
            ProductDTO productDTO = new ProductDTO(
                    product.getId(),
                    product.getTitle(),
                    product.getPrice(),
                    product.getDescription(),
                    product.getCategory().getId(),
                    product.getCategory().getName(),
                    new ArrayList<>()
            );

            Set<Picture> pictures = pictureRepository.findByProduct(product);
            for (Picture picture : pictures) {
                picture.setProduct(null);
                productDTO.getImageUrls().add(picture.getUrl());
            }

            product.setPicture(pictures);
            productDTOs.add(productDTO);
        }

        return new ResponseEntity<>(productDTOs, HttpStatus.OK);
    }

    @Operation(
            summary = "Get a product by ID",
            description = "Retrieve a single product by its ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);

        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ProductDTO productDTO = new ProductDTO(product.getId(),
                product.getTitle(), product.getPrice(), product.getDescription(), product.getCategory().getId(), product.getCategory().getName(), new ArrayList<>());

        Set<Picture> pictures = pictureRepository.findByProduct(product);

        // Пройти по найденным изображениям и сбросить ссылку на продукт
        for (Picture picture : pictures) {
            picture.setProduct(null);
            productDTO.getImageUrls().add(picture.getUrl());
        }

        product.setPicture(pictures);

        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }


    @Operation(
            summary = "Add a new product",
            description = "Add a new product to the database"
    )
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody ProductRequest productRequest) {
        // Создаем новый продукт на основе данных из запроса
        Product product = new Product();
        product.setTitle(productRequest.getTitle());
        product.setPrice(productRequest.getPrice());
        product.setDescription(productRequest.getDescription());
        product.setCategory(productService.getCategoryById(productRequest.getCategoryId()));

        // Создаем список объектов Picture на основе переданных URL изображений
        Set<Picture> pictures = new HashSet<>();
        for (String imageUrl : productRequest.getImages()) {
            Picture picture = new Picture();
            picture.setUrl(imageUrl);
            picture.setProduct(product); // Устанавливаем связь с продуктом
            pictures.add(picture);
        }
        product.setPicture(pictures);

        // Сохраняем продукт в базе данных
        Product savedProduct = productService.saveProduct(product);

        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update an existing product",
            description = "Update an existing product by its ID"
    )
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product existingProduct = productService.getProductById(id);
        if (existingProduct != null) {
            product.setId(id);
            Product updatedProduct = productService.saveProduct(product);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Delete a product",
            description = "Delete a product by its ID"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Product existingProduct = productService.getProductById(id);
        if (existingProduct != null) {
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
