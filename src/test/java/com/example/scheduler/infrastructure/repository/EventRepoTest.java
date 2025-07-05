package com.example.scheduler.infrastructure.repository;

import com.example.scheduler.AbstractTestContainerTest;
import com.example.scheduler.adapters.dto.CreateEventRequest;
import com.example.scheduler.domain.model.Event;
import com.example.scheduler.domain.model.EventType;
import com.example.scheduler.domain.model.User;
import com.example.scheduler.domain.repository.UserRepository;
import com.example.scheduler.infrastructure.mapper.EventMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@JdbcTest
@Import({EventRepositoryImpl.class,
        UserRepositoryImpl.class})
@ComponentScan("com.example.scheduler.infrastructure.mapper")
public class EventRepoTest extends AbstractTestContainerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EventRepositoryImpl eventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventMapper mapper;

    private UUID testEventId;
    private Event testEvent;

    @BeforeEach
    void setup() {

        CreateEventRequest eventRequest = new CreateEventRequest(
                "Title",
                "Description",
                EventType.ONE2ONE,
                1,
                30,
                10,
                10
        );

        User user = userRepository.save("test_user", "password", "email@exmpl.com");
        testEvent = eventRepository.save(mapper.toEntity(eventRequest, user.id()));

    }

    @Test
    @DisplayName("Regenerate slug in DB and check for equality")
    void shouldUpdateSlugInDatabase() {

        Event updatedEvent = eventRepository.regenerateSlug(testEvent.id());

        assertNotNull(updatedEvent);

        String actualSlug = jdbcTemplate.queryForObject(
                "SELECT slug FROM event_templates WHERE id = ?",
                String.class,
                testEvent.id()
        );

        assertEquals(updatedEvent.slug(), actualSlug);
    }
}
