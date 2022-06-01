package es.upm.miw.betca_tpv_user.data.daos;

import es.upm.miw.betca_tpv_user.data.model.Role;
import es.upm.miw.betca_tpv_user.data.model.Sex;
import es.upm.miw.betca_tpv_user.data.model.User;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DatabaseStarting {

    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_EMAIL = "admin@admin.com";
    private static final String ADMIN_PASSWORD = "admin";

    private static final String PROF_USER = "professional";
    private static final String PROF_EMAIL = "professional@professional.com";
    private static final String PROF_PASSWORD = "professional";

    private static final String PAT_USER = "patient";
    private static final String PAT_EMAIL = "patient@patient.com";
    private static final String PAT_PASSWORD = "patient";

    private final UserRepository userRepository;

    @Autowired
    public DatabaseStarting(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.initialize();
    }

    void initialize() {
        if (this.userRepository.findByRoleIn(List.of(Role.ADMIN, Role.PROFESSIONAL)).isEmpty()) {
            User admin = User.builder().email(ADMIN_EMAIL).firstName(ADMIN_USER)
                    .familyName(ADMIN_USER)
                    .sex(Sex.MALE)
                    .password(new BCryptPasswordEncoder().encode(ADMIN_PASSWORD))
                    .role(Role.PROFESSIONAL).role(Role.ADMIN).active(true).build();
            this.userRepository.save(admin);
            LogManager.getLogger(this.getClass()).warn("------- Created Admin -----------");
        }

        if (this.userRepository.findByRoleIn(List.of(Role.PROFESSIONAL)).isEmpty()) {
            User professional = User.builder().email(PROF_EMAIL).firstName(PROF_USER)
                    .familyName(PROF_USER)
                    .sex(Sex.MALE)
                    .password(new BCryptPasswordEncoder().encode(PROF_PASSWORD))
                    .role(Role.PROFESSIONAL).active(true).build();
            this.userRepository.save(professional);
            LogManager.getLogger(this.getClass()).warn("------- Created Professional -----------");
        }

        if (this.userRepository.findByRoleIn(List.of(Role.PATIENT)).isEmpty()) {
            User patient = User.builder().email(PAT_EMAIL).firstName(PAT_USER)
                    .familyName(PAT_USER)
                    .sex(Sex.MALE)
                    .password(new BCryptPasswordEncoder().encode(PAT_PASSWORD))
                    .role(Role.PATIENT).active(true).build();
            this.userRepository.save(patient);
            LogManager.getLogger(this.getClass()).warn("------- Created Patient -----------");
        }
    }

}
