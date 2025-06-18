package com.example.scheduler.domain.repository;

import com.example.scheduler.domain.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryImplTest {

    @Autowired
    private UserRepository repository;

    static PostgreSQLContainer<?> postgres;

    static {
        postgres = new PostgreSQLContainer<>("postgres:15")
                .withDatabaseName("testDb")
                .withUsername("user")
                .withPassword("password");
        postgres.start();
    }

    @BeforeAll
    static void startContainer() {
        postgres.start();
    }

    @DynamicPropertySource
    static void config(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void userSaveAndFind() {

        User user = new User(UUID.randomUUID(),
                "testName",
                "email@email.com",
                "password",
                true,
                Set.of(role));

        repository.save(user);

        Optional<User> dbUser = repository.findByUsername(user.username());
        assertTrue(dbUser.isPresent());
        assertAll("All fields matches",
                () -> assertEquals(dbUser.get().id(), user.id(), "ID match"),
                () -> assertEquals(dbUser.get().username(), user.username(), "LOGIN match"),
                () -> assertEquals(dbUser.get().email(), user.email(), "EMAIL match"),
                () -> assertEquals(dbUser.get().passwordHash(), user.passwordHash(), "PASSWORD match"),
                () -> assertEquals(dbUser.get().role(), user.role(), "ROLES match")
        );
    }
}