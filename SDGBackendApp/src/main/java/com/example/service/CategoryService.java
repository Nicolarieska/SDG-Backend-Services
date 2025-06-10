
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
        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new IllegalArgumentException("Category With Name '" + request.getName() + "' Already Exists.");
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setPoint(request.getPoint());

        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category With ID " + id + " Not Found."));
    }

    public Category updateCategory(int id, CategoryRequest request) {
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
    }

    public void deleteCategory(int id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category With ID " + id + " Not Found."));

        categoryRepository.delete(category);
    }
}
