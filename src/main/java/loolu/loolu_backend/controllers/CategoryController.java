package loolu.loolu_backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import loolu.loolu_backend.models.Category;
import loolu.loolu_backend.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Category Controller", description = "API for managing product categories")
@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Get all categories", description = "Retrieve a list of all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of categories returned successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @Operation(summary = "Get category by ID", description = "Retrieve a category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(
            @Parameter(description = "ID of the category to retrieve") @PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            return ResponseEntity.ok(category);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new category", description = "Create a new category with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping
    public ResponseEntity<Category> createCategory(
            @Parameter(description = "Details of the category to create") @RequestBody Category category) {
        Category createdCategory = categoryService.saveCategory(category);
        return ResponseEntity.status(201).body(createdCategory);
    }

    @Operation(summary = "Update an existing category", description = "Update the details of an existing category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @Parameter(description = "ID of the category to update") @PathVariable Long id,
            @Parameter(description = "Updated details of the category") @RequestBody Category categoryDetails) {
        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            category.setName(categoryDetails.getName());
            category.setImage(categoryDetails.getImage());
            Category updatedCategory = categoryService.saveCategory(category);
            return ResponseEntity.ok(updatedCategory);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a category", description = "Delete a category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "ID of the category to delete") @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
