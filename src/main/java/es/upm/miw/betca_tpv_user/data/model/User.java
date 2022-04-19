package es.upm.miw.betca_tpv_user.data.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Data //@ToString, @EqualsAndHashCode, @Getter, @Setter, @RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "healthtrackerUser") // conflict with user table
public class User {
    @Id
    @GeneratedValue
    private int id;
    @NonNull
    @Column(unique = true, nullable = false)
    private String email;
    @NonNull
    private String firstName;
    private String familyName;
    @Enumerated(EnumType.STRING)
    private Sex sex;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean active;
}
