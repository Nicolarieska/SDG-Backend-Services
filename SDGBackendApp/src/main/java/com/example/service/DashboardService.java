/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.service;

import com.example.repository.TrashDepositRepository;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Asus
 */
@Service
public class DashboardService {

    @Autowired
    private TrashDepositRepository repo;

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> resp = new LinkedHashMap<>();
        try {
            resp.put("totalUsers", repo.countUsers());
            resp.put("totalAdmins", repo.countAdmins());
            resp.put("totalCategories", repo.countCategories());
            resp.put("totalUserTransactions", repo.countUserTrashTransactions());
            resp.put("success", true);  // tambahkan terakhir
        } catch (Exception e) {
            resp.clear();
            resp.put("success", false);
            resp.put("message", "Failed to fetch dashboard stats: " + e.getMessage());
        }
        return resp;
    }
}
