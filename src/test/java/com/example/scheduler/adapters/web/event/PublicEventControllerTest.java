package com.example.scheduler.adapters.web.event;

import com.example.scheduler.adapters.dto.PublicEventResponse;
import com.example.scheduler.application.service.PublicEventService;
import com.example.scheduler.domain.model.EventType;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.nio.charset.StandardCharsets;
import java.util.TimeZone;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WebMvcTest(PublicEventController.class)
public class PublicEventControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private PublicEventService publicEventService;

    @Test
    public void getEventBySharedLinkTest() throws Exception {
        PublicEventResponse testResponse = new PublicEventResponse("abc", 10, EventType.GROUP, TimeZone.getTimeZone("GMT+3:00"));
        when(publicEventService.getEventBySharedLink(Mockito.anyString())).thenReturn(testResponse);

        mockMvc.perform(get("/api/public/events/{sharedLink}", UUID.randomUUID().toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.duration", is(10), int.class)
                );
    }
}
