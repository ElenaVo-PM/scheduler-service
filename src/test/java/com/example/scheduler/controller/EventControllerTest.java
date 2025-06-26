package com.example.scheduler.controller;

import com.example.scheduler.AbstractTestContainerTest;
import com.example.scheduler.domain.dto.ShortEventDto;
import com.example.scheduler.domain.service.EventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class EventControllerTest extends AbstractTestContainerTest {
    private final EventService eventService;
    private final WebApplicationContext webApplicationContext;
    private final MockMvc mockMvc;


    @Autowired
    public EventControllerTest(EventService eventService, WebApplicationContext webApplicationContext) {
        this.eventService = eventService;
        this.webApplicationContext = webApplicationContext;
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @MockitoBean
    public Principal principal;

    @Autowired
    ApplicationContext context;


    @Test
    @Sql("classpath:scripts/addUserAndSomeEvents")
    void getSomeEventsForExistingUser() {
        List<ShortEventDto> events = eventService.getAllEventsForCurrentUser("anna@user.ru");
        Assertions.assertEquals(2, events.size());
    }

    //тест не рабочий, контроллер почему-то не видит Principal
    @Test
    @Sql("classpath:scripts/addUserAndSomeEvents")
    void getSomeEventsForExistingUser2() throws Exception {
        Mockito.when(principal.getName()).thenReturn("anna@user.ru");
        mockMvc.perform(MockMvcRequestBuilders.get("/api/events"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) content().contentType(MediaType.APPLICATION_JSON));
    }

}