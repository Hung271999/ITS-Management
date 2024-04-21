package com.sharp.vn.its.management.controller;

import com.sharp.vn.its.management.dto.ResponseAPI;
import com.sharp.vn.its.management.dto.UserDto;
import com.sharp.vn.its.management.entity.RoleEntity;
import com.sharp.vn.its.management.entity.TokenEntity;
import com.sharp.vn.its.management.entity.UserEntity;
import com.sharp.vn.its.management.exception.DataValidationException;
import com.sharp.vn.its.management.repository.RoleRepository;
import com.sharp.vn.its.management.repository.TokenRepository;
import com.sharp.vn.its.management.repository.UserRepository;
import com.sharp.vn.its.management.security.UserDetailsImpl;
import com.sharp.vn.its.management.service.UserManagementService;
import com.sharp.vn.its.management.utils.JwtUtils;
import org.antlr.v4.runtime.Token;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.HashSet;
import java.util.Set;

@RestController()
@RequestMapping(value = "/auth")
public class AuthenticationController {

    @Autowired
    private UserManagementService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;


    @PostMapping("/signup")
    public ResponseAPI<?> registerUser(@Valid @RequestBody UserDto request) {
        service.saveUser(request);
        return new ResponseAPI<>(Boolean.TRUE);
    }

    @PostMapping("/login")
    public ResponseAPI<?> authenticateUser(@RequestBody UserDto loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();
        String userName = userDetails.getUsername();
        TokenEntity token = tokenRepository.findByUserId(userId).orElse(new TokenEntity());
        String jwt = token.getToken();
        if (jwt == null || !jwtUtils.validateToken(jwt)) {
            jwt = jwtUtils.generateToken(userDetails);
            token.setUser(userRepository.findById(userId).get());
            token.setToken(jwt);
            token.setCreatedBy(userName);
            token.setUpdatedBy(userName);
            tokenRepository.save(token);
        }
        UserDto userDto = new UserDto();
        userDto.setUserName(userDetails.getUsername());
        userDto.setToken(jwt);
        return new ResponseAPI<>(userDto);
    }


}
