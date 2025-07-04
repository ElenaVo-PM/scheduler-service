package com.example.scheduler.infrastructure.mapper;

import com.example.scheduler.adapters.dto.CreateEventRequest;
import com.example.scheduler.adapters.dto.EventFullDto;
import com.example.scheduler.adapters.dto.EventResponse;
import com.example.scheduler.domain.model.Event;
import com.example.scheduler.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    String EVENT_SHARE_LINK_PATTERN = "/api/public/event/%s";

    @Mapping(target = "ownerId", source = "ownerId")
    @Mapping(target = "isActive", expression = "java(true)")
    Event toEntity(CreateEventRequest request, UUID ownerId);

    @Mapping(target = "shareLink", source = "slug", qualifiedByName = "getShareLink")
    EventResponse toResponse(Event event);

    @Named("getShareLink")
    default String getShareLink(String slug) {
        return String.format(EVENT_SHARE_LINK_PATTERN, slug);
    }

    public static EventFullDto toEventFullDto(Event event, User user) {
        return new EventFullDto(
                event.id(),
                event.ownerId(),
                event.title(),
                event.description(),
                event.durationMinutes(),
                event.bufferBeforeMinutes(),
                event.bufferAfterMinutes(),
                event.maxParticipants(),
                event.isActive(),
                event.eventType(),
                event.slug(),
                event.startDate(),
                event.endDate(),
                event.createdAt(),
                event.updatedAt()
        );
    }
}
