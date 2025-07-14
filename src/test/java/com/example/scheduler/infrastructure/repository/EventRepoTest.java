package com.example.scheduler.infrastructure.repository;

import com.example.scheduler.AbstractTestContainerTest;
import com.example.scheduler.adapters.dto.CreateEventRequest;
import com.example.scheduler.adapters.dto.EventShortDto;
import com.example.scheduler.domain.model.Event;
import com.example.scheduler.domain.model.EventType;
import com.example.scheduler.domain.model.User;
import com.example.scheduler.domain.repository.UserRepository;
import com.example.scheduler.infrastructure.mapper.EventMapper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    @Test
    @DisplayName("getAllEvents")
    void getAllEvents() {
        userRepository.save("test_user2", "password", "email2@exmpl.com");
        UUID ownerId = userRepository.findByUsername("test_user2").get().id();
        Instant start = Instant.now().plus(10, ChronoUnit.MINUTES);
        Instant finish = start.plus(40, ChronoUnit.MINUTES);
        Instant now = Instant.now();
        Event activeEvent1 = new Event(null, ownerId, "title1", "description1", true,
                10, 100, 0, 0, EventType.GROUP, "slug1",
                start, finish, now, now);
        activeEvent1 = eventRepository.save(activeEvent1);

        Event activeEvent2 = new Event(null, ownerId, "title2", "description2", true,
                10, 100, 0, 0, EventType.GROUP, "slug2",
                start, finish, now, now);
        activeEvent2 = eventRepository.save(activeEvent2);

        Event notActiveEvent3 = new Event(null, ownerId, "title3", "description3", false,
                10, 100, 0, 0, EventType.GROUP, "slug3",
                start, finish, now, now);
        notActiveEvent3 = eventRepository.save(notActiveEvent3);

        Set<UUID> idSet = new HashSet<>();
        idSet.add(activeEvent1.id());
        idSet.add(activeEvent2.id());

        List<Event> activeOwnerEvents = eventRepository.getAllEvents(ownerId);
        List<EventShortDto> shortDtoList = mapper.toEventShortDtoList(activeOwnerEvents);

        assertEquals(idSet.size(), activeOwnerEvents.size());
        assertTrue(idSet.contains(activeOwnerEvents.get(0).id()));
        assertTrue(idSet.contains(activeOwnerEvents.get(1).id()));

        assertEquals(idSet.size(), shortDtoList.size());
        assertTrue(idSet.contains(shortDtoList.get(0).id()));
        assertTrue(idSet.contains(shortDtoList.get(1).id()));

    }
}
