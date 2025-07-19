package com.example.scheduler.domain.fixture;

import com.example.scheduler.domain.model.Credential;

import java.time.LocalDateTime;
import java.util.UUID;

public final class TestCredentials {

    private TestCredentials() {
        throw new AssertionError();
    }

    public static Credential alice() {
        return new Credential(
                UUID.fromString("d3e68c3b-2d6d-48a1-a037-99a390e9433e"),
                "alice",
                "{noop}12345",
                "USER",
                true,
                LocalDateTime.parse("2001-02-03T04:05:06.789012"),
                LocalDateTime.parse("2001-02-03T04:05:06.789012")
        );
    }

    public static Credential bob() {
        return new Credential(
                UUID.fromString("9e7f7e33-4574-43b6-83d8-ded7f169c03f"),
                "bob",
                "{noop}54321",
                "USER",
                true,
                LocalDateTime.parse("2002-03-04T05:06:07.890123"),
                LocalDateTime.parse("2002-03-04T05:06:07.890123")
        );
    }

    public static Credential charlie() {
        return new Credential(
                UUID.fromString("f089b61d-26e9-419f-9481-df5854a5312c"),
                "charlie",
                "{noop}12345",
                "USER",
                true,
                LocalDateTime.parse("2003-04-05T06:07:08.901234"),
                LocalDateTime.parse("2003-04-05T06:07:08.901234")
        );
    }

    public static Credential vasiliy() {
        return new Credential(
                UUID.fromString("8c91e5d4-72e4-4921-bc40-c917711f2e65"),
                "Vasiliy",
                "weakPassword",
                "USER",
                true,
                LocalDateTime.parse("2025-07-19T15:21:38.233204"),
                LocalDateTime.parse("2025-07-19T15:21:38.233204")
        );
        }
    }

