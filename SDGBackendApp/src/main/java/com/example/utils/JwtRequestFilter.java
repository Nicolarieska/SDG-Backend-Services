/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.utils;

import com.example.service.UserAuthService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author Asus
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserAuthService uds;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest hsr, HttpServletResponse hsr1, FilterChain fc) throws ServletException, IOException {
        String h = hsr.getHeader("Authorization");
        if (h == null || h.startsWith("Bearer ") == false) {
            fc.doFilter(hsr, hsr1);
            return;
        }

        String token = h.substring(7);
        String username = jwtUtil.extractUsername(token);
        if (username == null && SecurityContextHolder.getContext().getAuthentication() != null) {
            fc.doFilter(hsr, hsr1);
            return;
        }

        UserDetails ud = uds.loadUserByUsername(username);
        if (jwtUtil.validateToken(token, ud) == false) {
            fc.doFilter(hsr, hsr1);
            return;
        }

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                ud, null, ud.getAuthorities());

        auth.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(hsr));
        SecurityContextHolder.getContext().setAuthentication(auth);
        fc.doFilter(hsr, hsr1);
    }

}
