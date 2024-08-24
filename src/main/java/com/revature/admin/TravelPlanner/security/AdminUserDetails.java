package com.revature.admin.TravelPlanner.security;

import com.revature.admin.TravelPlanner.enums.AdminRole;
import com.revature.admin.TravelPlanner.models.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AdminUserDetails implements UserDetails {

    private Admin admin;

    public AdminUserDetails(Admin user) {
        this.admin = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(admin.isMaster()){
            authorities.add(new SimpleGrantedAuthority(AdminRole.ROLE_MASTER.name()));
        }
        else {
            authorities.add(new SimpleGrantedAuthority(AdminRole.ROLE_ADMIN.name()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    @Override
    public String getUsername() {
        return admin.getEmail();
    }
}
