package com.example.scheduler.domain.model;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;

@Component
public class EventRowMapper implements RowMapper<Event> {

    @Override
    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Event(
                rs.getObject("id", UUID.class),
                rs.getObject("owner_id", UUID.class),
                rs.getString("title"),
                rs.getString("description"),
                rs.getBoolean("is_active"),
                rs.getInt("max_participants"),
                rs.getInt("duration_minutes"),
                rs.getInt("buffer_before_minutes"),
                rs.getInt("buffer_after_minutes"),
                rs.getBoolean("is_group_event")?EventType.GROUP:EventType.ONE2ONE,
                rs.getString("slug"),
                rs.getObject("start_date", Instant.class),
                rs.getObject("end_date", Instant.class),
                rs.getObject("created_at", Instant.class),
                rs.getObject("updates_at", Instant.class)
        );
    }
}
