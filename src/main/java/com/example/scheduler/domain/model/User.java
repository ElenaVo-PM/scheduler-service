package com.example.scheduler.domain.model;

import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

public record User(
    UUID id,
    String publicUserName, //имя, которое отображается для других пользователей
    String email,
    String password,
    TimeZone userTimeZone, //временная зона пользователя
    Set<UserRole> roles
) {}