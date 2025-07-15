package com.example.scheduler.infrastructure.repository;

import com.example.scheduler.domain.model.Credential;
import com.example.scheduler.domain.model.User;
import com.example.scheduler.domain.repository.UserRepository;
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

        final String QUERY = "SELECT * FROM users WHERE UPPER(username) = UPPER(?)";

        try {
            Optional<User> dbUser = Optional.ofNullable(
                    jdbc.queryForObject(QUERY,
                            (res, num) -> new User(res.getObject("id", UUID.class),
                                    res.getString("username"),
                                    res.getString("email")),
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

        final String QUERY = """
                INSERT INTO users (id, email, password_hash, created_at, username, role)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        jdbc.update(QUERY, UUID.randomUUID(), email, password, LocalDateTime.now(), username, "USER");

        return findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public Optional<Credential> getCredential(String username) {

        final String QUERY = "SELECT * FROM users WHERE UPPER(username) = UPPER(?)";

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd HH:mm:ss")
                .optionalStart()
                .appendFraction(ChronoField.MICRO_OF_SECOND, 1, 6, true)
                .optionalEnd()
                .toFormatter();

        try {
            return Optional.ofNullable(
                    jdbc.queryForObject(QUERY,
                            (res, num) -> new Credential(res.getObject("id", UUID.class),
                                    res.getString("username"),
                                    res.getString("password_hash"),
                                    res.getString("role"),
                                    true,
                                    LocalDateTime.parse(res.getString("created_at"), formatter),
                                    LocalDateTime.parse(res.getString("updated_at"), formatter)), username));

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
