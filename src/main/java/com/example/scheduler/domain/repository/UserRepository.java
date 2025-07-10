package com.example.scheduler.domain.repository;

import com.example.scheduler.domain.exception.EmailAlreadyExistException;
import com.example.scheduler.domain.exception.UsernameAlreadyExistException;
import com.example.scheduler.domain.model.User;
import com.example.scheduler.domain.model.UserGeneralInfo;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User insert(User user) throws UsernameAlreadyExistException, EmailAlreadyExistException;

    Optional<User> findByUsername(String username);

    User save(String username, String password, String email);

    Optional<Credential> getCredential(String username);

    Optional<UserGeneralInfo> findUserGeneralInfoById(UUID id);

    Optional<User> findByEmail(String email);
}
