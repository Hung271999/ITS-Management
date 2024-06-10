package com.sharp.vn.its.management.controller;

import com.sharp.vn.its.management.dto.UserDTO;
import com.sharp.vn.its.management.service.UserManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * The type User controller.
 */
@RestController()
@RequestMapping(value = "/users")
public class UserController extends BaseController {

    /**
     * The Service.
     */
    @Autowired
    private UserManagementService service;

    /**
     * Register user user dto.
     *
     * @param request the request
     * @return the user dto
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO registerUser(@Valid @RequestBody UserDTO request) {
        return service.saveUser(request);
    }

    /**
     * Update user user dto.
     *
     * @param userId the user id
     * @param request the request
     * @return the user dto
     */
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO updateUser(@PathVariable Long userId, @Valid @RequestBody UserDTO request) {
        return service.saveUser(request);
    }

    /**
     * Delete user response entity.
     *
     * @param userId the user id
     * @return the response entity
     */
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        service.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

}
