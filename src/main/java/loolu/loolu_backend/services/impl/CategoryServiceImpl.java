package loolu.loolu_backend.services.impl;

import jakarta.transaction.Transactional;
import loolu.loolu_backend.models.Category;
import loolu.loolu_backend.repositories.CategoryRepository;
import loolu.loolu_backend.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        try {
            return categoryRepository.findAll();
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to retrieve all categories", e);
        }
    }

    @Override
    public Category getCategoryById(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        return optionalCategory.orElse(null);
    }

    @Override
    public Category saveCategory(Category category) {
        try {
            return categoryRepository.save(category);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to save category: " + category.getName(), e);
        }
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        try {
            categoryRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to delete category with ID: " + id, e);
        }
    }

    @Override
    public List<Category> findCategoriesByName(String name) {
        try {
            return categoryRepository.findByNameContainingIgnoreCase(name);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to find categories by name: " + name, e);
        }
    }
}