/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.service;

import com.example.dto.TrashDepositRequest;
import com.example.model.Category;
import com.example.model.TrashDeposit;
import com.example.model.User;
import com.example.repository.CategoryRepository;
import com.example.repository.TrashDepositRepository;
import com.example.repository.UserRepository;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Asus
 */
@Service
public class TrashDepositService {

    @Autowired
    private TrashDepositRepository tdRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public TrashDeposit createTrashDeposit(TrashDepositRequest request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User Not Found With Username: " + username));

            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category With ID " + request.getCategoryId() + " Not Found."));

            TrashDeposit create = new TrashDeposit();
            create.setUser(user);
            create.setCategory(category);
            create.setWeight(request.getWeight());
            create.setDescription(request.getDescription());
            create.setStatus(null);
            create.setApprover(null);
            create.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            return tdRepository.save(create);
        } catch (Exception e) {
            throw new RuntimeException("Failed to Create Trash Deposit: " + e.getMessage(), e);
        }
    }
}
