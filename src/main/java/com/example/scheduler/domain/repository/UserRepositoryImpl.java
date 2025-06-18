package com.example.scheduler.domain.repository;

import com.example.scheduler.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
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

        String query = "SELECT * FROM users WHERE email = ?";

        try {
            Optional<User> dbUser = Optional.ofNullable(
                    jdbc.queryForObject(query,
                            (res, rowNum) -> new User(res.getObject("id", UUID.class),
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
    public User save(User user) {

        String query = """
                INSERT INTO users (id, login, email, password_hash)
                VALUES (?, ?, ?, ?)
                """;

        jdbc.update(query,
                user.id(),
                user.username(),
                user.email(),
                user.passwordHash());


        return findByUsername(user.username())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
