
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.service;

import com.example.dto.CategoryRequest;
import com.example.model.Category;
import com.example.repository.CategoryRepository;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Asus
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(CategoryRequest request) {
        try {
            if (categoryRepository.findByName(request.getName()).isPresent()) {
                throw new IllegalArgumentException("Category With Name '" + request.getName() + "' Already Exists.");
            }

            Category category = new Category();
            category.setName(request.getName());
            category.setDescription(request.getDescription());
            category.setPoint(request.getPoint());

            return categoryRepository.save(category);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create category: " + e.getMessage(), e);
        }
    }

    public List<Category> getAllCategories() {
        try {
            return categoryRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch categories: " + e.getMessage(), e);
        }
    }

    public Category getCategoryById(int id) {
        try {
            return categoryRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Category With ID " + id + " Not Found."));
        } catch (Exception e) {
            throw new RuntimeException("Failed to get category by ID: " + e.getMessage(), e);
        }
    }

    public Category updateCategory(int id, CategoryRequest request) {
        try {
            Category existingCategory = categoryRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Category with ID " + id + " not found."));

            if (!existingCategory.getName().equals(request.getName())
                    && categoryRepository.findByName(request.getName()).isPresent()) {
                throw new IllegalArgumentException("Category with name '" + request.getName() + "' Already Exists.");
            }

            existingCategory.setName(request.getName());
            existingCategory.setDescription(request.getDescription());
            existingCategory.setPoint(request.getPoint());

            return categoryRepository.save(existingCategory);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update category: " + e.getMessage(), e);
        }
    }

    public void deleteCategory(int id) {
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Category With ID " + id + " Not Found."));

            categoryRepository.delete(category);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete category: " + e.getMessage(), e);
        }
    }
}
