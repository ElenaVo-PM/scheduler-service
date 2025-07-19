package com.example.scheduler.infrastructure.repository;

import com.example.scheduler.domain.fixture.TestEvents;
import com.example.scheduler.domain.fixture.TestUsers;
import com.example.scheduler.domain.model.Event;
import com.example.scheduler.infrastructure.mapper.EventMapperImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@JdbcTest
@Import({EventRepositoryImpl.class,
        EventMapperImpl.class})
class EventRepoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EventRepositoryImpl eventRepository;

    @Test
    @DisplayName("Regenerate slug in DB and check for equality")
    void shouldUpdateSlugInDatabase() {

        Event updatedEvent = eventRepository.regenerateSlug(TestEvents.demo().id());

        assertNotNull(updatedEvent);

        String actualSlug = jdbcTemplate.queryForObject(
                "SELECT slug FROM event_templates WHERE id = ?",
                String.class,
                TestEvents.demo().id()
        );

        assertEquals(updatedEvent.slug(), actualSlug);
    }

    @Test
    @DisplayName("getAllEvents")
    void getAllEvents() {

        List<Event> activeOwnerEvents = eventRepository.getAllEvents(TestUsers.ALICE.id());

        then(activeOwnerEvents).containsExactlyInAnyOrder(TestEvents.demo(), TestEvents.daily());
    }
}
