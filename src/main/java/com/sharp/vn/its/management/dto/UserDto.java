package com.sharp.vn.its.management.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharp.vn.its.management.constants.ITSRole;
import com.sharp.vn.its.management.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Data
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private Long id;
    private String userName;

    @NotBlank(message = "Password invalid value")
    @Length(message = "Password must be 6 character", min = 6)
    private String password;

    private String email;
    @NotBlank(message = "First name invalid value")
    private String firstName;

    @NotBlank(message = "Last name invalid value")
    private String lastName;

    private String token;

    @NotNull(message = "Role invalid value")
    private ITSRole role;

    public UserDto(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.userName = userEntity.getUsername();
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.email = userEntity.getEmail();
        this.role = userEntity.getRoles().stream().findFirst().get().getRoleName();
    }

    public UserDto() {

    }
}
