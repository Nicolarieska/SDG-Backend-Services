/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.repository;

import com.example.model.TrashDeposit;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Asus
 */
public interface TrashDepositRepository extends JpaRepository<TrashDeposit, Integer> {

    List<TrashDeposit> findByUserId(int userId);

    List<TrashDeposit> findByApproverId(int approverId);

    List<TrashDeposit> findByStatus(String status);
}
