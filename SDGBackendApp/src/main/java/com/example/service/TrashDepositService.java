/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.service;

import com.example.dto.TrashDepositRequest;
import com.example.dto.TrashDepositValidationRequest;
import com.example.model.Category;
import com.example.model.TrashDeposit;
import com.example.model.User;
import com.example.repository.CategoryRepository;
import com.example.repository.TrashDepositRepository;
import com.example.repository.UserRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.transaction.Transactional;
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
            create.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

            return tdRepository.save(create);
        } catch (Exception e) {
            throw new RuntimeException("Failed to Create Trash Deposit: " + e.getMessage(), e);
        }
    }

    public TrashDeposit validateTrashDeposit(TrashDepositValidationRequest req) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User admin = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Admin Not Found: " + username));

            TrashDeposit deposit = tdRepository.findById(req.getDepositId())
                    .orElseThrow(() -> new RuntimeException(
                    "TrashDeposit Id " + req.getDepositId() + " Not Found"));

            String upper = req.getStatus().trim().toUpperCase();
            if (!upper.equals("DITERIMA") && !upper.equals("DITOLAK") && !upper.equals("DIPENDING")) {
                throw new IllegalArgumentException("Status must be DITERIMA, DITOLAK, or DIPENDING");
            }

            deposit.setApprover(admin);
            deposit.setStatus(upper);
            deposit.setDescription(req.getDescription());
            deposit.setCreatedAt(Timestamp.valueOf(LocalDateTime.now())); // refresh tanggal

            return tdRepository.save(deposit);

        } catch (Exception e) {
            throw new RuntimeException("Failed to Validate Trash Deposit: " + e.getMessage(), e);
        }
    }

    public Map<String, Object> getHistoryUser(String username) {
        List<Object[]> rows = tdRepository.findAllRows(username);

        List<Map<String, Object>> tx = new ArrayList<>();
        for (Object[] r : rows) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", r[0]);
            m.put("categoryName", r[1]);
            m.put("weight", r[2]);
            m.put("pointPerKg", r[3]);
            m.put("transactionPoint", r[4]);
            m.put("status", r[5]);
            m.put("createdAt", r[6]);
            tx.add(m);
        }

        Integer total = tdRepository.sumAcceptedPoint(username);

        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("totalPointAccepted", total);
        resp.put("transactions", tx);
        return resp;
    }
}
