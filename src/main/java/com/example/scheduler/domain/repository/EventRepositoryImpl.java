package com.example.scheduler.domain.repository;

import com.example.scheduler.domain.model.Event;
import com.example.scheduler.domain.model.EventRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class EventRepositoryImpl implements EventRepository {
    private final JdbcTemplate jdbcTemplate;
    private final EventRowMapper eventRowMapper;

    @Autowired
    public EventRepositoryImpl(JdbcTemplate jdbcTemplate, EventRowMapper eventRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.eventRowMapper = eventRowMapper;
    }

    @Override
    public Optional<Event> getEventBySharedLink(UUID sharedLink) {
        final String QUERY = "SELECT * FROM event_templates WHERE slug = ?";
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(QUERY, eventRowMapper, sharedLink));
    }
}
