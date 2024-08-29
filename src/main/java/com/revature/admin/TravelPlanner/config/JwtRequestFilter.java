package com.revature.admin.TravelPlanner.config;

import com.revature.admin.TravelPlanner.security.AdminUserDetails;
import com.revature.admin.TravelPlanner.security.AdminUserDetailsService;
import com.revature.admin.TravelPlanner.security.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    // Autowiring the JwtTokenProvider to validate and extract information from JWT tokens
    @Autowired
    JwtTokenProvider jwtProvider;

    // Autowiring the custom AdminUserDetailsServiceImpl to load user details by user ID
    @Autowired
    AdminUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Extracting the Authorization header from the HTTP request
        String authHeader = request.getHeader("Authorization");
        String token = null;
        UUID adminId = null;

        // Checking if the Authorization header is present and starts with "Bearer "
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            // Extracting the token from the Authorization header (excluding the "Bearer " prefix)
            token = authHeader.substring(7);
            // Extracting the admin ID from the token using the JwtTokenProvider
            adminId = UUID.fromString(jwtProvider.extractUserId(token));
        }

        // Checking if the admin ID is not null and there is no authentication already set in the security context
        if(adminId != null && SecurityContextHolder.getContext().getAuthentication() == null){

            // Loading the admin user details by user ID
            AdminUserDetails adminUserDetails = userDetailsService.loadUserByUserId(adminId);

            // Validating the token with the extracted admin ID
            if(jwtProvider.validateToken(token,adminId.toString())){

                // Creating an authentication token with the admin user's details and their authorities
                UsernamePasswordAuthenticationToken authToken;
                authToken = new UsernamePasswordAuthenticationToken(adminUserDetails, null, adminUserDetails.getAuthorities());

                // Setting the authentication details from the current HTTP request
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Setting the authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Proceeding with the next filter in the chain
        filterChain.doFilter(request,response);
    } // doFilterInternal
}
