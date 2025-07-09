package com.example.scheduler.adapters.mapper;

import com.example.scheduler.adapters.dto.ProfileResponse;
import com.example.scheduler.domain.model.Profile;
import org.mapstruct.Mapper;

@Mapper
public interface ProfileMapper {
    ProfileResponse toDto(Profile profile, String username);
}
