package com.example.scheduler.domain.service;

import com.example.scheduler.domain.dto.ShortEventDto;

import java.util.List;

public interface EventService {

    List<ShortEventDto> getAllEventsForCurrentUser(String email);

}
