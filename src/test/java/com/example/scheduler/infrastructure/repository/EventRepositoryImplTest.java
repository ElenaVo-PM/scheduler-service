package com.example.scheduler.infrastructure.repository;

import com.example.scheduler.domain.model.Event;
import com.example.scheduler.domain.repository.EventRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@ContextConfiguration(classes = EventRepositoryImpl.class)
public class EventRepositoryImplTest {
    @Autowired
    private EventRepository repository;

    @Test
    @DisplayName("getEventById")
    @Sql(scripts = "classpath:scripts/data-test.sql")
    void getEventByIdTest() {

        Optional<Event> dbEvent = repository.getEventById(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));

        assertTrue(dbEvent.isPresent());

        assertAll("Проверка полей",
                () -> assertEquals(dbEvent.get().id(), UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), "id не совпадает."),
                () -> assertEquals(dbEvent.get().ownerId(), UUID.fromString("be3cc829-d8b4-43af-985a-b5a282c88723"), "ownerId не совпадает."),
                () -> assertEquals(dbEvent.get().description(), "Description for event 1", "Описание не совпадает.")
        );
    }

}
