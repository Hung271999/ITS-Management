package com.sharp.vn.its.management.security;

import com.sharp.vn.its.management.entity.UserEntity;
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
     * The Encoder.
     * -- SETTER --
     *  Sets encoder.
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Load user by username: {}", username);
        final UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username: " + username));
        final String password = encoder.encode(user.getPassword());
        user.setPassword(password);
        return new UserSecurityDetails(user);
    }

}
