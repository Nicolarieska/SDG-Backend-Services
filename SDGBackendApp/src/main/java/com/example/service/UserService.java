/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.service;

import com.example.dto.AdminResponse;
import com.example.dto.RegisterRequest;
import com.example.dto.UpdateProfileRequest;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.utils.JwtUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Asus
 *
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService uds;

    public Map<String, Object> register(RegisterRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                response.put("message", "Username is Already Taken");
                response.put("success", false);
                return response;
            }

            User user = new User();
            user.setName(request.getName());
            user.setPhone(request.getPhone());
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole("user");

            userRepository.save(user);

            UserDetails userDetails = uds.loadUserByUsername(user.getUsername());
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", user.getRole());

            String token = jwtUtil.generateToken(userDetails, claims);

            response.put("message", "User Registered Successfully");
            response.put("token", token);
            response.put("success", true);
        } catch (Exception e) {
            response.put("message", "Registration Failed: " + e.getMessage());
            response.put("success", false);
        }

        return response;
    }

    public Map<String, Object> updateUserProfile(UpdateProfileRequest req) {
        Map<String, Object> resp = new HashMap<>();
        try {
            String username
                    = SecurityContextHolder.getContext().getAuthentication().getName();

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (req.getName() != null) {
                user.setName(req.getName());
            }
            if (req.getPhone() != null) {
                user.setPhone(req.getPhone());
            }

            userRepository.save(user);

            Map<String, Object> data = new HashMap<>();
            data.put("name", user.getName());
            data.put("username", user.getUsername());
            data.put("phone", user.getPhone());
            data.put("role", user.getRole());

            resp.put("success", true);
            resp.put("message", "Profile Updated");
            resp.put("data", data);
        } catch (Exception e) {
            resp.put("success", false);
            resp.put("message", e.getMessage());
        }
        return resp;
    }

    public Map<String, Object> getUserProfile() {
        Map<String, Object> resp = new HashMap<>();
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Map<String, Object> data = new HashMap<>();
            data.put("name", user.getName());
            data.put("username", user.getUsername());
            data.put("phone", user.getPhone());
            data.put("role", user.getRole());

            resp.put("success", true);
            resp.put("data", data);
        } catch (Exception e) {
            resp.put("success", false);
            resp.put("message", e.getMessage());
        }
        return resp;
    }
}
