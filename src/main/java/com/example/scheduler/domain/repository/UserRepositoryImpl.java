package com.example.scheduler.domain.repository;

import com.example.scheduler.domain.model.Credential;
import com.example.scheduler.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbc;

    @Autowired
    public UserRepositoryImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Optional<User> findByUsername(String username) {

        String query = "SELECT * FROM users WHERE user_login = ?";

        try {
            Optional<User> dbUser = Optional.ofNullable(
                    jdbc.queryForObject(query,
                            (res, _) -> new User(res.getObject("id", UUID.class),
                                    res.getString("user_login"),
                                    res.getString("full_name"),
                                    res.getString("email"),
                                    TimeZone.getTimeZone(res.getString("timezone"))),
                            username)
            );

            if (dbUser.isPresent()) {
                return dbUser;
            }

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

        return Optional.empty();
    }

    @Transactional
    @Override
    public User save(String username, String password, String email) {

        //Тут мы вроде как должны фулл наме добавить, но черт его знает зачем

        String query = """
                INSERT INTO users (id, email, password_hash, created_at, full_name, user_login, role)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        jdbc.update(query, UUID.randomUUID(), email, password, LocalDateTime.now(), "unknown", username, "USER");

        return findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public Optional<Credential> getCredential(String username) {

        String query = "SELECT * FROM users WHERE user_login = ?";
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd HH:mm:ss")
                .optionalStart()
                .appendFraction(ChronoField.MICRO_OF_SECOND, 1, 6, true)
                .optionalEnd()
                .toFormatter();

        try {
            Optional<Credential> creditsDb = Optional.ofNullable(
                    jdbc.queryForObject(query,
                            (res, _) -> new Credential(res.getObject("id", UUID.class),
                                    res.getString("user_login"),
                                    res.getString("password_hash"),
                                    res.getString("role"),
                                    true,
                                    LocalDateTime.parse(res.getString("created_at"), formatter),
                                    LocalDateTime.parse(res.getString("updated_at"), formatter)), username));

            return creditsDb;

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
