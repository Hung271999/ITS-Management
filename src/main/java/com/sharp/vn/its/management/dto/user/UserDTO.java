package com.sharp.vn.its.management.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharp.vn.its.management.constants.Role;
import com.sharp.vn.its.management.entity.RoleEntity;
import com.sharp.vn.its.management.entity.UserEntity;
import com.sharp.vn.its.management.entity.UserRoleEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * The type User dto.
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    /**
     * The Id.
     */
    private Long userId;

    /**
     * The User name.
     */
    private String userName;

    /**
     * The Password.
     */
    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    /**
     * The Email.
     */
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    private String email;

    /**
     * The First name.
     */
    @NotBlank(message = "First name must not be blank")
    private String firstName;

    /**
     * The Last name.
     */
    @NotBlank(message = "Last name must not be blank")
    private String lastName;

    /**
     * The Full name.
     */
    private String fullName;

    /**
     * The Role.
     */
    @NotNull(message = "Role must not be null")
    private Role role;

    /**
     * The Token.
     */
    private String token;


    /**
     * Instantiates a new User dto.
     *
     * @param userEntity the user entity
     */
    public UserDTO(UserEntity userEntity) {
        this.userId = userEntity.getId();
        this.userName = userEntity.getUsername();
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.fullName = this.firstName + " " + this.lastName;
        this.email = userEntity.getEmail();
        this.role = userEntity.getRoles().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("User does not have any roles"))
                .getRole()
                .getRoleName();
    }

    /**
     * Instantiates a new User dto.
     *
     * @param userName the user name
     * @param token the token
     */
    public UserDTO(final String userName, final String token) {
        this.userName = userName;
        this.token = token;
    }

    /**
     * Instantiates a new User dto.
     *
     * @param userId the user id
     * @param userName the user name
     */
    public UserDTO(final long userId, final String userName, final String firstName,
            final String lastName) {
        this.userId = userId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
    }
}
