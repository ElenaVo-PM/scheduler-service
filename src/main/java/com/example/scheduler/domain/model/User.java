package com.example.scheduler.domain.model;

import java.util.TimeZone;

public record User(
    Long id,
    String publicUserName, //имя, которое отображается для других пользователей
    String email,
    TimeZone userTimeZone //временная зона пользователя
) {}