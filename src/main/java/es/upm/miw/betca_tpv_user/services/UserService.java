package es.upm.miw.betca_tpv_user.services;

import es.upm.miw.betca_tpv_user.data.daos.UserRepository;
import es.upm.miw.betca_tpv_user.data.model.Role;
import es.upm.miw.betca_tpv_user.data.model.User;
import es.upm.miw.betca_tpv_user.services.exceptions.ConflictException;
import es.upm.miw.betca_tpv_user.services.exceptions.ForbiddenException;
import es.upm.miw.betca_tpv_user.services.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    public UserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public Optional< String > login(String email) {
        return this.userRepository.findByEmail(email)
                .map(user -> jwtService.createToken(user.getEmail(), user.getFirstName(), user.getRole().name()));
    }

    public void createUser(User user) {
        this.assertNoExistByEmail(user.getEmail());
        this.userRepository.save(user);
    }

//    public Stream< User > readAll(Role roleClaim) {
//        return this.userRepository.findByRoleIn(authorizedRoles(roleClaim)).stream();
//    }

    private List< Role > authorizedRoles(Role roleClaim) {
        if (Role.ADMIN.equals(roleClaim)) {
            return List.of(Role.ADMIN, Role.PROFESSIONAL);
        } else if (Role.PROFESSIONAL.equals(roleClaim)) {
            return List.of(Role.PROFESSIONAL);
        } else if (Role.PATIENT.equals(roleClaim)) {
            return List.of(Role.PATIENT);
        } else {
            return List.of();
        }
    }

    private void assertNoExistByEmail(String email) {
        if (this.userRepository.findByEmail(email).isPresent()) {
            throw new ConflictException("The email already exists: " + email);
        }
    }

//    public Stream< User > findByMobileAndFirstNameAndFamilyNameAndEmailAndDniContainingNullSafe(
//            String mobile, String firstName, String familyName, String email, String dni, Role roleClaim) {
//        return this.userRepository.findByMobileAndFirstNameAndFamilyNameAndEmailAndDniContainingNullSafe(
//                mobile, firstName, familyName, email, dni, this.authorizedRoles(roleClaim)
//        ).stream();
//    }
//
//    public User readByEmailAssured(String email) {
//        return this.userRepository.findByEmail(email)
//                .orElseThrow(() -> new NotFoundException("The email don't exist: " + email));
//    }
}
