package com.example.scheduler.domain.model;

import java.util.TimeZone;
import java.util.UUID;

public record User(
        UUID id,
        String username,
        String publicName, //имя, которое отображается для других пользователей
        String email,
        TimeZone userTimeZone //временная зона пользователя
) {}