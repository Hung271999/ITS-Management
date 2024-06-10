package com.sharp.vn.its.management.service;

import com.sharp.vn.its.management.constants.ITSRole;
import com.sharp.vn.its.management.dto.UserDTO;
import com.sharp.vn.its.management.entity.RoleEntity;
import com.sharp.vn.its.management.entity.UserEntity;
import com.sharp.vn.its.management.exception.DataValidationException;
import com.sharp.vn.its.management.exception.ObjectNotFoundException;
import com.sharp.vn.its.management.repositories.RoleRepository;
import com.sharp.vn.its.management.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * The type User management service.
 */
@Service
@Slf4j
public class UserManagementService {
    /**
     * The User repository.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * The Role repository.
     */
    @Autowired
    private RoleRepository roleRepository;

    /**
     * Save user user dto.
     *
     * @param request the request
     * @return the user dto
     */
    public UserDTO saveUser(UserDTO request) {
        log.info("Saving task...");
        final String userName = request.getUserName();
        final String email = request.getEmail();
        // Check if username or email already exists
        if (StringUtils.isEmpty(userName) || userRepository.existsByUsername(userName)) {
            throw new DataValidationException("Username " + userName + " already exists");
        }
        if (StringUtils.isEmpty(email) || userRepository.existsByEmail(email)) {
            throw new DataValidationException("Email " + email + " already exists");
        }
        // set properties
        UserEntity user = new UserEntity();
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new DataValidationException("User ID not found"));
        }
        user.setUsername(request.getUserName());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        final ITSRole itsRole = request.getRole();
        final RoleEntity role = roleRepository.findByRoleName(itsRole)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Can't find role with name: " + itsRole.name()));
        user.setRoles(Collections.singleton(role));
        UserDTO result = new UserDTO(userRepository.save(user));
        log.info("User saved successfully.");
        return result;

    }

    /**
     * Delete user.
     *
     * @param id the id
     */
    public void deleteUser(Long id) {
        if (id == null) {
            throw new DataValidationException("User ID is null or empty");
        }
        userRepository.deleteById(id);
        log.info("User with id {} deleted successfully.", id);
    }
}
