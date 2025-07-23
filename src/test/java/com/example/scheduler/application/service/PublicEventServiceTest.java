package com.example.scheduler.application.service;

import com.example.scheduler.adapters.dto.PublicEventResponse;
import com.example.scheduler.domain.model.Event;
import com.example.scheduler.domain.model.EventType;
import com.example.scheduler.domain.model.UserGeneralInfo;
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
        Event event = new Event(UUID.randomUUID(), UUID.randomUUID(), "abc", "defg", true,
                10, 1000, 15, 100, EventType.GROUP,
                UUID.randomUUID().toString(), Instant.now(), Instant.now().plusSeconds(10),
                Instant.now().minusSeconds(2000), Instant.now().minusSeconds(1500));
        when(eventRepository.getEventBySlug(Mockito.any(UUID.class))).thenReturn(Optional.of(event));
        when(userRepository.findUserGeneralInfoById(Mockito.any(UUID.class))).thenReturn(Optional.of(new UserGeneralInfo(UUID.randomUUID(),
                "zxc", "111@example.com", "zcb", TimeZone.getDefault())));
        PublicEventResponse response = publicEventService.getEventBySlug(UUID.randomUUID().toString());

        assertEquals("abc", response.title());
        assertEquals(1000, response.duration());
        assertEquals(EventType.GROUP, response.groupEvent());
    }
}
