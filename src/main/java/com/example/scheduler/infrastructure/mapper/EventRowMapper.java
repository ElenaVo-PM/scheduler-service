package com.example.scheduler.infrastructure.mapper;

import com.example.scheduler.domain.model.Event;
import com.example.scheduler.domain.model.EventType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class EventRowMapper implements RowMapper<Event> {

    @Override
    public Event mapRow(ResultSet res, int rowNum) throws SQLException {
        return new Event(
                res.getObject("id", UUID.class),
                res.getObject("ownerId", UUID.class),
                res.getString("title"),
                res.getString("description"),
                res.getBoolean("isActive"),
                res.getInt("maxParticipants"),
                res.getInt("durationMinutes"),
                res.getInt("bufferBeforeMinutes"),
                res.getInt("bufferAfterMinutes"),
                res.getBoolean("is_group_event") ? EventType.GROUP : EventType.ONE2ONE,
                res.getString("slug"),
                res.getTimestamp("startDate").toInstant(),
                res.getTimestamp("endDate") == null
                        ? null
                        : res.getTimestamp("endDate").toInstant(),
                res.getTimestamp("createdAt").toInstant(),
                res.getTimestamp("updatedAt").toInstant()
        );
    }
}
