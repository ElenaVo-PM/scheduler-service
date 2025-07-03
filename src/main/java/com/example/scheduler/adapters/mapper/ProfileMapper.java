package com.example.scheduler.adapters.mapper;

import com.example.scheduler.adapters.dto.ProfileResponse;
import com.example.scheduler.domain.model.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    @Mapping(target = "username", source = "username")
    ProfileResponse toDto(Profile profile, String username);
}