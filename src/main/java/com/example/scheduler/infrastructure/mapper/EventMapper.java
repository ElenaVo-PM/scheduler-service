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
    @Mapping(target = "startDate", expression = "java(Instant.now())")
    @Mapping(target = "endDate", expression = "java(java.time.Instant.now().plus(java.time.Duration.ofMinutes(request.durationMinutes())))")
    @Mapping(target = "createdAt", expression = "java(Instant.now())")
    @Mapping(target = "updatedAt", expression = "java(Instant.now())")
    Event toEntity(CreateEventRequest request, UUID ownerId);

    @Mapping(target = "shareLink", source = "slug", qualifiedByName = "getShareLink")
    EventResponse toResponse(Event event);

    @Named("getShareLink")
    default String getShareLink(String slug) {
        return String.format(EVENT_SHARE_LINK_PATTERN, slug);
    }

    @Mapping(target = "id", source = "event.id")
    @Mapping(target = "ownerId", source = "event.ownerId")
    @Mapping(target = "title", source = "event.title")
    @Mapping(target = "description", source = "event.description")
    @Mapping(target = "durationMinutes", source = "event.durationMinutes")
    @Mapping(target = "bufferBeforeMinutes", source = "event.bufferBeforeMinutes")
    @Mapping(target = "bufferAfterMinutes", source = "event.bufferAfterMinutes")
    @Mapping(target = "maxParticipants", source = "event.maxParticipants")
    @Mapping(target = "isActive", source = "event.isActive")
    @Mapping(target = "eventType", source = "event.eventType")
    @Mapping(target = "slug", source = "event.slug")
    @Mapping(target = "startDate", source = "event.startDate")
    @Mapping(target = "endDate", source = "event.endDate")
    @Mapping(target = "createdAt", source = "event.createdAt")
    @Mapping(target = "updatedAt", source = "event.updatedAt")
    EventFullDto toEventFullDto(Event event, User user);
}
