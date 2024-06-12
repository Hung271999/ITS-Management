package com.sharp.vn.its.management.security;

import com.sharp.vn.its.management.entity.UserEntity;
import com.sharp.vn.its.management.exception.AuthenticationException;
import com.sharp.vn.its.management.repositories.UserRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * The type User details service.
 */
@Service
@Slf4j
public class SecurityService implements UserDetailsService {

    /**
     * The User repository.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * The Encoder. -- SETTER -- Sets encoder.
     *
     * @param encoder the encoder
     */
    @Setter
    private PasswordEncoder encoder;


    /**
     * Load user by username user details.
     *
     * @param username the username
     * @return the user details
     * @throws UsernameNotFoundException the username not found exception
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        final UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("Attempt to access non-existent user with username: {}", username);
            return new UsernameNotFoundException("User not found with provided username.");
        });
        final String password = encoder.encode(user.getPassword());
        user.setPassword(password);
        return new UserSecurityDetails(user);
    }

}
