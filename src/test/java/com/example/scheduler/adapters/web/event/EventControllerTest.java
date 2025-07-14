package com.example.scheduler.adapters.web.event;

import com.example.scheduler.AbstractTestContainerTest;
import com.example.scheduler.adapters.dto.EventShortDto;
import com.example.scheduler.application.service.EventService;
import com.example.scheduler.domain.model.Event;
import com.example.scheduler.domain.model.EventType;
import com.example.scheduler.infrastructure.mapper.EventMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class EventControllerTest extends AbstractTestContainerTest {
    private final EventService eventService;
    private final WebApplicationContext webApplicationContext;
    private final MockMvc mockMvc;
    private final EventMapper eventMapper;


    @Autowired
    public EventControllerTest(EventService eventService, WebApplicationContext webApplicationContext, EventMapper eventMapper) {
        this.eventService = eventService;
        this.webApplicationContext = webApplicationContext;
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.eventMapper = eventMapper;
    }

    @MockitoBean
    public Principal principal;

    @Autowired
    ApplicationContext context;

    @Test
    void getEventById_Success() throws Exception {
        Mockito.when(principal.getName()).thenReturn("user1@user.ru");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/events/{id}", "550e8400-e29b-41d4-a716-446655440000")
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("550e8400-e29b-41d4-a716-446655440000"))
                .andExpect(jsonPath("$.title").isNotEmpty());
    }

    @Test
    void getEventById_NotFound() throws Exception {
        Mockito.when(principal.getName()).thenReturn("user1@user.ru");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/events/{id}", "550e8400-e29b-41d4-a716-446655440010")
                        .principal(principal))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Событие не найдено.", result.getResolvedException().getMessage()));
    }

}
