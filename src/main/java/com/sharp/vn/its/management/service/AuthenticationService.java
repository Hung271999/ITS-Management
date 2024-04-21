package com.sharp.vn.its.management.service;

import com.sharp.vn.its.management.repository.RoleRepository;
import com.sharp.vn.its.management.repository.UserRepository;
import com.sharp.vn.its.management.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public UserDetails getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return userDetails;
        }
        return null;
    }
}
