package com.sharp.vn.its.management.controller;

import com.sharp.vn.its.management.dto.user.UserDTO;
import com.sharp.vn.its.management.service.UserManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.sharp.vn.its.management.filter.*;



import java.util.List;

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
    private CriteriaSearchRequest filter = new CriteriaSearchRequest();

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
     * @param userId  the user id
     * @param request the request
     * @return the user dto
     */
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO updateUser(@PathVariable Long userId, @Valid @RequestBody UserDTO request) {
        return service.saveUser(request);
    }

    /**
     * Gets user detail.
     *
     * @param userId the user id
     * @return the user detail
     */
    /* Gets task detail.
     *
     * @param taskId the task id
     * @return the task detail
     */
    @GetMapping("/{userId}")
    public UserDTO getUserDetail(@PathVariable(required = true) Long userId) {
        return service.getUserDetail(userId);
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

    /**
     * Load all users data list.
     *
     * @return the list
     */
    @GetMapping()
    public List<UserDTO> loadAllUsersData() {
        return service.getAllUsersData();
    }

    //    /**
//     * Search users with pagination.
//     *
//     * @param request the request
//     * @return the page of UserDTO
//     */
    @PostMapping("/all")
    public Page<UserDTO> loadAllUsers(@RequestBody UserDTO request) {
        return service.getListUsersData(request);
    }
}