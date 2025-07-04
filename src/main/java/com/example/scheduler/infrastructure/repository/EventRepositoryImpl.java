package com.example.scheduler.infrastructure.repository;

import com.example.scheduler.domain.model.Event;
import com.example.scheduler.domain.model.EventType;
import com.example.scheduler.domain.repository.EventRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class EventRepositoryImpl implements EventRepository {

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM event_templates WHERE id = ?";
    private static final String SAVE_QUERY = """
            INSERT INTO event_templates (id, user_id, title, description,
            duration_minutes, buffer_before_minutes, buffer_after_minutes,
            is_group_event, max_participants, is_active, slug)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private final JdbcTemplate jdbc;

    public EventRepositoryImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Optional<Event> findById(UUID id) {
        Event event = jdbc.queryForObject(FIND_BY_ID_QUERY,
                (res, _) -> new Event(res.getObject("id", UUID.class),
                        res.getObject("user_id", UUID.class),
                        res.getString("title"),
                        res.getString("description"),
                        res.getBoolean("is_active"),
                        res.getInt("duration_minutes"),
                        res.getInt("buffer_before_minutes"),
                        res.getInt("buffer_after_minutes"),
                        res.getBoolean("is_group_event") ? EventType.GROUP : EventType.ONE2ONE,
                        res.getInt("max_participants"),
                        res.getString("slug"),
                        res.getTimestamp("created_at").toInstant(),
                        res.getTimestamp("updated_at").toInstant()),
                id
        );

        return Optional.ofNullable(event);
    }

    @Override
    public Event save(Event e) {
        UUID id = UUID.randomUUID();
        jdbc.update(SAVE_QUERY, id, e.ownerId(), e.title(), e.description(),
                e.durationMinutes(), e.bufferBeforeMinutes(), e.bufferAfterMinutes(),
                EventType.GROUP.equals(e.eventType()), e.maxParticipants(), e.isActive(), UUID.randomUUID());

        return findById(id).orElseThrow();
    }
}