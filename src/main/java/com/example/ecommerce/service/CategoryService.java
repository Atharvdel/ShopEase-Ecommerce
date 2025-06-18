package com.example.ecommerce.service;

import com.example.ecommerce.model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Category saveCategory(Category category);
    List<Category> getAllCategories();
    Optional<Category> getCategoryById(Long id);
    Optional<Category> getCategoryByName(String name);
    List<Object[]> getAllCategoriesWithProductCount();
    void deleteCategory(Long id);
}
