/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 *
 * @author Asus
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Name is Required")
    @Size(max = 100, message = "Name Must Be Less Than 100 Characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Phone Number is Required")
    @Size(min = 10, max = 13, message = "Phone Number Must Be Between 10 and 13 Characters")
    @Column(nullable = false)
    private String phoneNumber;

    @NotBlank(message = "Username is Required")
    @Size(max = 50, message = "Username Must Be Less Than 50 Characters")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Password is Required")
    @Size(min = 8, message = "Password Must Be At Least 8 Characters")
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    public User() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
