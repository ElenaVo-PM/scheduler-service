package com.example.scheduler.adapters.web.event;

import com.example.scheduler.adapters.dto.CreateEventRequest;
import com.example.scheduler.adapters.dto.EventResponse;
import com.example.scheduler.application.service.EventService;
import com.example.scheduler.domain.model.EventType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecuredEventControllerWebMvcTest {

    private final CreateEventRequest request = new CreateEventRequest("title", "description",
            EventType.ONE2ONE, 1, 1, 0, 0);

    private final EventResponse response = new EventResponse(UUID.randomUUID(),
            "/api/public/event/%s".formatted(UUID.randomUUID()));

    @MockitoBean
    private EventService eventService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "MockUserDetailsService")
    public void givenCreateEventRequestWithAuthorizedUser_shouldSucceedWith201() throws Exception {

        when(eventService.createEvent(eq(request), any(UUID.class)))
                .thenReturn(response);

        mvc.perform(post("/api/events")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(eventService, times(1)).createEvent(eq(request), any(UUID.class));
    }

    @Test
    public void givenCreateEventRequestWithNonAuthorizedUser_shouldErrorWith4xx() throws Exception {

        mvc.perform(post("/api/events")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}
