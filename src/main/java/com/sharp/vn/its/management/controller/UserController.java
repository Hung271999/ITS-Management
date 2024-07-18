package com.sharp.vn.its.management.controller;

import com.sharp.vn.its.management.dto.chart.DataDTO;
import com.sharp.vn.its.management.dto.chart.RequestDTO;
import com.sharp.vn.its.management.dto.user.UserDTO;
import com.sharp.vn.its.management.service.UserManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;


import javax.persistence.Tuple;
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
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
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


    /**
     * Load all users page.
     *
     * @param request the request
     * @return the page
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/all")
    public Page<UserDTO> loadAllUsers(@RequestBody UserDTO request) {
        return service.getListUsersData(request);
    }

    /**
     * Test.
     *
     * @return the data dto
     */
    @PostMapping("/test")
    public DataDTO test(@RequestBody RequestDTO requestDTO){
        return service.getUserTaskData(requestDTO.getUserIds(), requestDTO.getYears());
    }
}
