package com.example.scheduler.domain.service;

import com.example.scheduler.domain.dto.ShortEventDto;
import com.example.scheduler.domain.mapper.EventMapper;
import com.example.scheduler.domain.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {
    private final UserService userService;
    private final EventRepository eventRepository;

    public EventServiceImpl(UserService userService, EventRepository eventRepository) {
        this.userService = userService;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<ShortEventDto> getAllEventsForCurrentUser(String email) {
        return userService.getUser(email)
                .map(user -> eventRepository.findAllByOwnerId(user.id()))
                .map(EventMapper::toShortEventDto)
                .orElseThrow(() -> new RuntimeException(String.format("пользователь c email=%s не найден", email)));
    }
}
