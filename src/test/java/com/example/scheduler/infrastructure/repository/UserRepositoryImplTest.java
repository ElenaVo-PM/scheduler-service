package com.example.scheduler.infrastructure.repository;

import com.example.scheduler.domain.model.Credential;
import com.example.scheduler.domain.model.User;
import com.example.scheduler.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ContextConfiguration(classes = UserRepositoryImpl.class)
class UserRepositoryImplTest {

    @Autowired
    private UserRepository repository;
    @Autowired
    private DataSource dataSource;

    @Test
    @DisplayName("Checking USERS exists")
    void usersTableShouldExist() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             ResultSet resultSet = connection.getMetaData()
                     .getTables(null, null, "users", new String[]{"TABLE"})) {

            boolean tableExists = resultSet.next();
            assertTrue(tableExists, "Таблица 'users' должна существовать в БД");
        }
    }

    @Test
    @DisplayName("Add new user and get the same from DB")
    void userSaveAndFind() {

        String username = "username00";
        String password = "password";
        String email = "email@ex.com";

        User newUser = repository.save(username, password, email);

        Optional<User> dbUser = repository.findByUsername(username);
        Optional<Credential> usersCredits = repository.getCredential(username);

        assertTrue(dbUser.isPresent());
        assertTrue(usersCredits.isPresent());

        assertAll("All fields matches",
                () -> assertEquals(dbUser.get().id(), newUser.id(), "ID match"),
                () -> assertEquals(dbUser.get().username(), newUser.username(), "LOGIN match"),
                () -> assertEquals(dbUser.get().email(), newUser.email(), "EMAIL match"),
                () -> assertEquals(password, usersCredits.get().getPassword(), "PASSWORD match")
        );
    }

    @Test
    @DisplayName("Get null on unknown user")
    void shouldThrowUserNotFound() {

        String username = "anyUser";

        assertTrue(repository.getCredential(username).isEmpty());
    }

    @Test
    void givenUserExist_WhenFindByUsernameInDifferentCase_ThenReturnOptionalWithUser() {
        String username = "username00";
        String password = "password";
        String email = "email@ex.com";
        repository.save(username, password, email);

        Optional<User> userO = repository.findByUsername(username.toUpperCase());

        then(userO).isNotEmpty();
    }

    @Test
    void givenUserExist_WhenSaveNewUserWithSameUsername_ThenThrowDuplicateKeyException() {
        String usernameA = "username00";
        String passwordA = "password";
        String emailA = "email@ex.com";
        String usernameB = "USERNAME00";
        String passwordB = "password01";
        String emailB = "alice@mail.com";
        repository.save(usernameA, passwordA, emailA);

        Throwable throwable = catchThrowable(() -> repository.save(usernameB, passwordB, emailB));

        then(throwable).isInstanceOf(DuplicateKeyException.class);
    }
}
