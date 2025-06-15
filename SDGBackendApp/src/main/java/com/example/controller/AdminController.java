/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller;

import com.example.dto.RegisterRequest;
import com.example.model.User;
import com.example.service.AdminService;
import com.example.service.UserService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Asus
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    @PreAuthorize("hasAuthority('admin')")
    public Map<String, Object> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('admin')")
    public Map<String, Object> createAdmin(@RequestBody RegisterRequest request) {
        return adminService.createAdmin(request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public Map<String, Object> deleteAdmin(@PathVariable int id) {
        return adminService.deleteAdmin(id);
    }
}
