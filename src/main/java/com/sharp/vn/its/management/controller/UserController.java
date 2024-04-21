package com.sharp.vn.its.management.controller;

import com.sharp.vn.its.management.dto.ResponseAPI;
import com.sharp.vn.its.management.dto.UserDto;
import com.sharp.vn.its.management.service.UserManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(value = "/users/management")
public class UserController {

    @Autowired
    private UserManagementService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto registerUser(@Valid @RequestBody UserDto request) {
        return service.saveUser(request);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto updateUser(@PathVariable Long userId, @Valid @RequestBody UserDto request) {
        return service.saveUser(request);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        service.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

}
