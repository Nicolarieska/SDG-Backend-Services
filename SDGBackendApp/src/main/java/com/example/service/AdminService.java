/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.service;

import com.example.dto.AdminResponse;
import com.example.dto.RegisterRequest;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.utils.JwtUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Asus
 */
@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService uds;

    public Map<String, Object> createAdmin(RegisterRequest request) {
        Map<String, Object> resp = new HashMap<>();
        try {
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                resp.put("success", false);
                resp.put("message", "Username is Already Taken");
                return resp;
            }

            User admin = new User();
            admin.setName(request.getName());
            admin.setPhone(request.getPhone());
            admin.setUsername(request.getUsername());
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
            admin.setRole("admin");

            userRepository.save(admin);

            resp.put("success", true);
            resp.put("message", "Admin Created");
            resp.put("adminId", admin.getId());
        } catch (Exception e) {
            resp.put("success", false);
            resp.put("message", "Failed to Create Admin: " + e.getMessage());
        }
        return resp;
    }

    public Map<String, Object> deleteAdmin(int id) {
        Map<String, Object> resp = new HashMap<>();
        try {
            User admin = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Admin Not Found"));

            if (!"admin".equals(admin.getRole())) {
                throw new RuntimeException("User is Not An Admin");
            }

            userRepository.delete(admin);
            resp.put("success", true);
            resp.put("message", "Admin Deleted");
        } catch (Exception e) {
            resp.put("success", false);
            resp.put("message", "Failed to Delete Admin: " + e.getMessage());
        }
        return resp;
    }

    public Map<String, Object> getAllAdmins() {
        Map<String, Object> resp = new HashMap<>();
        try {
            List<User> admins = userRepository.findByRole("admin");

            List<AdminResponse> dtoList = new ArrayList<>();
            for (User u : admins) {
                AdminResponse dto = new AdminResponse();
                dto.setId(u.getId());
                dto.setName(u.getName());
                dto.setPhone(u.getPhone());
                dto.setUsername(u.getUsername());
                dto.setRole(u.getRole());
                dtoList.add(dto);
            }

            resp.put("success", true);
            resp.put("data", dtoList);
        } catch (Exception e) {
            resp.put("success", false);
            resp.put("message", "Failed to Fetch Admins: " + e.getMessage());
        }
        return resp;
    }
}
