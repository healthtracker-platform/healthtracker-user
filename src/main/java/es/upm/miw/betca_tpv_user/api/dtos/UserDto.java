package es.upm.miw.betca_tpv_user.api.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import es.upm.miw.betca_tpv_user.data.model.Role;
import es.upm.miw.betca_tpv_user.data.model.Sex;
import es.upm.miw.betca_tpv_user.data.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    @NotNull
    @NotBlank
    @Pattern(regexp = Validations.EMAIL)
    private String email;
    @NotNull
    @NotBlank
    private String firstName;
    private String familyName;
    private Sex sex;
    private String password;
    private Role role;
    private Boolean active;

    public UserDto(User user) {
        BeanUtils.copyProperties(user, this);
        this.password = "secret";
    }

    public static UserDto ofMobileFirstName(User user) {
        return UserDto.builder().email(user.getEmail()).firstName(user.getFirstName()).familyName(user.getFamilyName()).active(user.getActive()).password(user.getPassword()).role(user.getRole()).sex(user.getSex()).build();
    }

    public void doDefault() {
        if (Objects.isNull(password)) {
            password = UUID.randomUUID().toString();
        }
        if (Objects.isNull(role)) {
            this.role = Role.PATIENT;
        }
        if (Objects.isNull(active)) {
            this.active = true;
        }
    }

    public User toUser() {
        this.doDefault();
        User user = new User();
        BeanUtils.copyProperties(this, user);
        user.setPassword(new BCryptPasswordEncoder().encode(this.password));
        return user;
    }
}
