/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller;

import com.example.dto.UpdateProfileRequest;
import com.example.service.UserService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Asus
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    @PreAuthorize("hasAuthority('user')")
    public Map<String, Object> getProfile() {
        return userService.getUserProfile();
    }

    @PutMapping()
    @PreAuthorize("hasAuthority('user')")
    public Map<String, Object> updateProfile(@RequestBody UpdateProfileRequest req) {
        return userService.updateUserProfile(req);
    }
}
