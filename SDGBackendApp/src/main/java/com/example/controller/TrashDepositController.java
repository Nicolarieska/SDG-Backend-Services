/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller;

import com.example.dto.TrashDepositRequest;
import com.example.dto.TrashDepositValidationRequest;
import com.example.model.TrashDeposit;
import com.example.service.TrashDepositService;
import com.example.service.UserService;
import java.security.Principal;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Asus
 */
@RestController
@RequestMapping("/api/trashDeposit")
public class TrashDepositController {

    @Autowired
    private UserService userService;

    @Autowired
    private TrashDepositService tdService;

    @PostMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> createTrashDeposit(@RequestBody TrashDepositRequest request) {
        try {
            TrashDeposit data = tdService.createTrashDeposit(request);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Failed to Create Trash Deposit: " + e.getMessage());
        }
    }

    @PutMapping("/validate")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> validateTrashDeposit(@RequestBody TrashDepositValidationRequest request) {
        try {
            TrashDeposit data = tdService.validateTrashDeposit(request);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to Validate Trash Deposit: " + e.getMessage());
        }
    }

    @GetMapping("/history")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> getSummary(Authentication auth) {
        String username = auth.getName();
        return ResponseEntity.ok(tdService.getHistoryUser(username));
    }
}
