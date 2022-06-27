package es.upm.miw.betca_tpv_user.api.resources;

import es.upm.miw.betca_tpv_user.api.dtos.TokenDto;
import es.upm.miw.betca_tpv_user.api.dtos.UserDto;
import es.upm.miw.betca_tpv_user.data.model.Role;
import es.upm.miw.betca_tpv_user.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(UserResource.USERS)
public class UserResource {
    public static final String USERS = "/users";
    public static final String TOKEN = "/token";
    public static final String EMAIL_ID = "/{email}";


    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @SecurityRequirement(name = "basicAuth")
    @PreAuthorize("authenticated")
    @PostMapping(value = TOKEN)
    public Optional< TokenDto > login(@AuthenticationPrincipal User activeUser) {
        return userService.login(activeUser.getUsername())
                .map(TokenDto::new);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public void createUser(@Valid @RequestBody UserDto creationUserDto) {
        this.userService.createUser(creationUserDto.toUser());
    }

    private Role extractRoleClaims() {
        List<String> roleClaims = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return Role.of(roleClaims.get(0));  // it must only be a role
    }

}
