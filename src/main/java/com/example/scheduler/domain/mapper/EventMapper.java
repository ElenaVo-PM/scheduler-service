package com.example.scheduler.domain.mapper;


import com.example.scheduler.domain.dto.ShortEventDto;
import com.example.scheduler.domain.model.Event;

import java.util.List;

public class EventMapper {

    public static ShortEventDto toShortEventDto(Event event) {
        return new ShortEventDto(
                event.id(),
                event.title(),
                event.isActive(),
                event.link(),
                event.isGroupEvent()
        );
    }

    public static List<ShortEventDto> toShortEventDto(List<Event> events) {
        return events.stream()
                .map(EventMapper::toShortEventDto)
                .toList();
    }
}
