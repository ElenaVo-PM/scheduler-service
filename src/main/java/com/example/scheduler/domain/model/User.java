package com.example.scheduler.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("users")
public record User(

    @Id
    UUID id,

    @Column("email")
    String email,

    @Column("password_hash")
    String passwordHash,

    @Column("full_name")
    String fullName,

    @Column("timezone")
    String userTimeZone, //временная зона пользователя

    @Column("created_at")
    LocalDateTime createdAt,

    @Column("updated_at")
    LocalDateTime updatedAt
) {
    public User(String email, String passwordHash, String fullName, String userTimeZone) {
        this(null, email, passwordHash, fullName, userTimeZone, null, null);
    }
}



