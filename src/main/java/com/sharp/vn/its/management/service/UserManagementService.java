package com.sharp.vn.its.management.service;

import com.sharp.vn.its.management.constants.Role;
import com.sharp.vn.its.management.dto.user.UserDTO;
import com.sharp.vn.its.management.entity.RoleEntity;
import com.sharp.vn.its.management.entity.UserEntity;
import com.sharp.vn.its.management.entity.UserRoleEntity;
import com.sharp.vn.its.management.exception.DataValidationException;
import com.sharp.vn.its.management.exception.ObjectNotFoundException;
import com.sharp.vn.its.management.repositories.RoleRepository;
import com.sharp.vn.its.management.repositories.UserRepository;
import com.sharp.vn.its.management.security.UserSecurityDetails;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type User management service.
 */
@Service
@Slf4j
public class UserManagementService extends BaseService {
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
     * The Authentication service.
     */
    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Save user user dto.
     *
     * @param request the request
     * @return the user dto
     */
    @Transactional
    public UserDTO saveUser(UserDTO request) {
        log.info("Saving task...");
        final String userName = request.getUserName();
        final String email = request.getEmail();
        final Long userId = request.getUserId();
        // Check if username or email already exists
        if (StringUtils.isEmpty(userName) || userRepository.existsByUsername(userName,
                userId)) {
            throw new DataValidationException("Username " + userName + " already exists");
        }
        if (StringUtils.isEmpty(email) || userRepository.existsByEmail(email, userId)) {
            throw new DataValidationException("Email " + email + " already exists");
        }
        // set properties
        UserEntity user = new UserEntity();
        if (userId != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new DataValidationException("User ID not found"));
        }
        user.setUsername(request.getUserName());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setFullName(user.getFirstName() + " " + user.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        UserSecurityDetails authenticatedUser = authenticationService.getUser();
        if (authenticatedUser != null) {
            UserEntity currentUser = userRepository.findById(authenticatedUser.getId())
                    .orElseThrow(() -> new ObjectNotFoundException(
                            "User not found with id " + authenticatedUser.getId()));
            user.setCreatedBy(currentUser);
            user.setUpdatedBy(currentUser);
        }
        final Role itsRole = request.getRole();
        final RoleEntity role = roleRepository.findByRoleName(itsRole)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Can't find role with name: " + itsRole.name()));
        UserRoleEntity userRole = new UserRoleEntity(user, role);
        user.getRoles().add(userRole);
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

    /**
     * Gets all users data.
     *
     * @return the all users data
     */
    public List<UserDTO> getAllUsersData() {
        log.info("Fetching all users...");
        final List<UserDTO> users = userRepository.findAll().stream()
                .map(userEntity -> new UserDTO(userEntity.getId(), userEntity.getUsername(),
                        userEntity.getFirstName(), userEntity.getLastName()))
                .toList();
        log.info("All users fetched successfully.");
        return users;
    }
}
