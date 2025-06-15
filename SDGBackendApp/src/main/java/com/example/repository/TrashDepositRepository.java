/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.repository;

import com.example.model.TrashDeposit;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Asus
 */
public interface TrashDepositRepository extends JpaRepository<TrashDeposit, Integer> {

    List<TrashDeposit> findByUserId(int userId);

    List<TrashDeposit> findByApproverId(int approverId);

    List<TrashDeposit> findByStatus(String status);

    @Query("SELECT td.id, c.name, td.weight, c.point, "
            + "(c.point * td.weight), td.status, td.createdAt "
            + "FROM TrashDeposit td JOIN td.category c "
            + "WHERE td.user.username = :username "
            + "ORDER BY td.createdAt DESC")
    List<Object[]> findAllRows(@Param("username") String username);

    @Query("SELECT COALESCE(SUM(c.point * td.weight), 0) "
            + "FROM TrashDeposit td JOIN td.category c "
            + "WHERE td.user.username = :username "
            + "  AND td.status = 'DITERIMA'")
    Integer sumAcceptedPoint(@Param("username") String username);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'user'")
    Integer countUsers();

    @Query("SELECT COUNT(c) FROM Category c")
    Integer countCategories();

    @Query("SELECT COUNT(td) FROM TrashDeposit td WHERE td.user.role = 'user'")
    Long countUserTrashTransactions();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'admin'")
    Integer countAdmins();
}
