package com.example.scheduler.application.service;

import com.example.scheduler.adapters.dto.PublicEventResponse;
import com.example.scheduler.domain.model.Event;
import com.example.scheduler.domain.model.EventType;
import com.example.scheduler.domain.model.User;
import com.example.scheduler.domain.repository.EventRepository;
import com.example.scheduler.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PublicEventServiceTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private UserRepository userRepository;
    private PublicEventService publicEventService;

    @BeforeEach
    public void setup() {
        publicEventService = new PublicEventService(eventRepository, userRepository);
    }

    @Test
    void getEventBySharedLinkTest() {
        Event event = new Event(UUID.randomUUID(), UUID.randomUUID(), "abc", "defg", 1000, true, 100, EventType.GROUP,
                UUID.randomUUID(), Instant.now(), Instant.now().plusSeconds(10));
        when(eventRepository.getEventBySharedLink(Mockito.any(UUID.class))).thenReturn(Optional.of(event));
        when(userRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(new User(UUID.randomUUID(),
                "zxc", "zcb", "111@example.com", TimeZone.getDefault())));
        PublicEventResponse response = publicEventService.getEventBySharedLink(UUID.randomUUID().toString());

        assertEquals("abc", response.title());
        assertEquals(1000, response.duration());
        assertEquals(EventType.GROUP, response.groupEvent());
    }
}
