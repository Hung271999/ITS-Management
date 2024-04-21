package com.sharp.vn.its.management.service;

import com.sharp.vn.its.management.dto.ResponseAPI;
import com.sharp.vn.its.management.dto.UserDto;
import com.sharp.vn.its.management.entity.RoleEntity;
import com.sharp.vn.its.management.entity.UserEntity;
import com.sharp.vn.its.management.exception.DataValidationException;
import com.sharp.vn.its.management.repository.RoleRepository;
import com.sharp.vn.its.management.repository.UserRepository;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserManagementService {
    @Autowired
    private PasswordEncoder encoder;


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public UserDto saveUser(UserDto request) {
        final String userName = request.getUserName();
        final String email = request.getEmail();
        if (StringUtils.isEmpty(userName) || userRepository.existsByUsername(userName)) {
            throw new DataValidationException("User name invalid value");
        }
        if (StringUtils.isEmpty(email) || userRepository.existsByEmail(email)) {
            throw new DataValidationException("Email invalid value");
        }

        UserEntity user = new UserEntity();
        if (request.getId() != null) {
            user = userRepository.findById(request.getId()).orElseThrow(() -> new DataValidationException("User id not found"));
        }
        user.setUsername(request.getUserName());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPasswordEncode(encoder.encode(request.getPassword()));
        user.setPassword(request.getPassword());

        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity role = roleRepository.findByRoleName(request.getRole())
                .orElseThrow(() -> new DataValidationException("Role invalid value"));
        roles.add(role);
        user.setRoles(roles);
        return new UserDto(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        if (id == null) {
            throw new DataValidationException("User id invalid");
        }
        userRepository.deleteById(id);
    }
}
