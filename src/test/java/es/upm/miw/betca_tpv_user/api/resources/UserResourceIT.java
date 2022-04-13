package es.upm.miw.betca_tpv_user.api.resources;

import es.upm.miw.betca_tpv_user.api.dtos.UserDto;
import es.upm.miw.betca_tpv_user.data.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static es.upm.miw.betca_tpv_user.api.resources.UserResource.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ApiTestConfig
class UserResourceIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private RestClientTestService restClientTestService;

    @Test
    void testLogin() {
        this.restClientTestService.loginAdmin(this.webTestClient);
        assertTrue(this.restClientTestService.getToken().length() > 10);
    }

/*    @Test
    void testReadUser() {
        this.restClientTestService.loginAdmin(this.webTestClient)
                .get().uri(USERS + EMAIL_ID, "superadmin@superadmin.com")
                .exchange().expectStatus().isOk()
                .expectBody(UserDto.class)
                .value(user -> assertEquals("c1", user.getFirstName()));
    }*/

    @Test
    void testReadUserNotFound() {
        this.restClientTestService.loginAdmin(this.webTestClient)
                .get().uri(USERS + EMAIL_ID, "usernotfound@usernotfound.com")
                .exchange().expectStatus().isNotFound();
    }

    @Test
    void testReadUserForbidden() {
        this.restClientTestService.loginPatient(this.webTestClient)
                .get().uri(USERS + EMAIL_ID, "patient2@patient.com")
                .exchange().expectStatus().isUnauthorized();
    }

/*
    @Test
    void testCreateUserWithAdmin() {
        this.restClientTestService.loginAdmin(this.webTestClient)
                .post().uri(USERS)
                .body(Mono.just(UserDto.builder().email("admin@admin.com").firstName("daemon").build()), UserDto.class)
                .exchange().expectStatus().isOk();
    }
*/


/*    @Test
    void testCreateUserUnauthorizedNoLogin() {
        this.webTestClient
                .post().uri(USERS)
                .body(Mono.just(UserDto.builder().email("c2@gmail.com").firstName("daemon").build()), UserDto.class)
                .exchange().expectStatus().isUnauthorized();
    }*/



/*    @Test
    void testCreateAdminUserForbidden() {
        this.restClientTestService.loginPatient(this.webTestClient)
                .post().uri(USERS)
                .body(Mono.just(UserDto.builder().email("c2@gmail.com").firstName("daemon").role(Role.ADMIN).build()),
                        UserDto.class)
                .exchange().expectStatus().isForbidden();
    }*/

/*    @Test
    void testCreateUserConflict() {
        this.restClientTestService.loginAdmin(this.webTestClient)
                .post().uri(USERS)
                .body(Mono.just(UserDto.builder().email("c2@gmail.com").firstName("daemon").build()), UserDto.class)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }*/

/*    @Test
    void testCreateFullUser() {
        this.restClientTestService.loginAdmin(this.webTestClient)
                .post().uri(USERS)
                .body(Mono.just(UserDto.builder().email("c123@gmail.com").firstName("daemon").familyName("family")
                        .address("address").password("123").collegiateNumber("dni").build()), UserDto.class)
                .exchange().expectStatus().isOk();
    }*/

    @Test
    void testCreateUserBadNumber() {
        this.restClientTestService.loginProfessional(this.webTestClient)
                .post().uri(USERS)
                .body(Mono.just(UserDto.builder().email("6").firstName("kk").build()), UserDto.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testCreateUserWithoutNumber() {
        this.restClientTestService.loginAdmin(this.webTestClient)
                .post().uri(USERS)
                .body(Mono.just(UserDto.builder().email(null).firstName("kk").build()), UserDto.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

/*    @Test
    void testReadAllOperator() {
        this.restClientTestService.loginPatient(this.webTestClient)
                .get().uri(USERS)
                .exchange().expectStatus().isOk()
                .expectBodyList(UserDto.class)
                .value(users -> assertTrue(users.stream().noneMatch(user -> "patient2@patient.com".equals(user.getEmail()))))
                .value(users -> assertTrue(users.stream().noneMatch(user -> "c2@gmail.com".equals(user.getEmail()))));
    }*/

    @Test
    void testReadAllPatient() {
        this.restClientTestService.loginPatient(this.webTestClient)
                .get().uri(USERS)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    /*@Test
    void testSearch() {
        this.restClientTestService.loginProfessional(this.webTestClient)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(USERS + SEARCH)
                        .queryParam("mobile", "6")
                        .queryParam("firstName", "c").build())
                .exchange().expectStatus().isOk()
                .expectBodyList(UserDto.class)
                .value(users -> assertTrue(users.stream().anyMatch(user -> "c1".equals(user.getFirstName()))));
    }*/

}
