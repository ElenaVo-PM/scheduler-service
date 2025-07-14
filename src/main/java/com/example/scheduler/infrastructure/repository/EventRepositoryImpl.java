package com.example.scheduler.infrastructure.repository;

import com.example.scheduler.domain.model.Event;
import com.example.scheduler.domain.model.EventType;
import com.example.scheduler.domain.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EventRepositoryImpl implements EventRepository {

    private static final String SAVE_QUERY = """
            INSERT INTO event_templates (id, user_id, title, description,
            duration_minutes, buffer_before_minutes, buffer_after_minutes,
            is_group_event, max_participants, is_active, slug, start_date,
            end_date, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_SLUG_QUERY = """
            UPDATE event_templates
            SET slug = ?
            WHERE id = ?
            """;

    private static final String FIND_BY_ID_QUERY = """
                SELECT
                    id,
                    user_id AS ownerId,
                    title,
                    description,
                    is_active AS isActive,
                    max_participants AS maxParticipants,
                    duration_minutes AS durationMinutes,
                    buffer_before_minutes AS bufferBeforeMinutes,
                    buffer_after_minutes AS bufferAfterMinutes,
                    is_group_event,
                    slug,
                    start_date AS startDate,
                    end_date AS endDate,
                    created_at AS createdAt,
                    updated_at AS updatedAt
                FROM event_templates
                WHERE id = ?
            """;
    private static final String UPDATE_QUERY = """
            UPDATE event_templates SET title = ?, description = ?,
            duration_minutes = ?, buffer_before_minutes = ?, buffer_after_minutes = ?,
            is_group_event = ?, max_participants = ?, is_active = ?,
            start_date = ?, end_date = ?, updated_at = now()
            WHERE id = ?
            """;

    private static final String GET_ALL_ACTIVE_EVENTS = """
                SELECT
                    id,
                    user_id AS ownerId,
                    title,
                    description,
                    is_active AS isActive,
                    max_participants AS maxParticipants,
                    duration_minutes AS durationMinutes,
                    buffer_before_minutes AS bufferBeforeMinutes,
                    buffer_after_minutes AS bufferAfterMinutes,
                    is_group_event,
                    slug,
                    start_date AS startDate,
                    end_date AS endDate,
                    created_at AS createdAt,
                    updated_at AS updatedAt
                FROM event_templates
                WHERE user_id = ? AND is_active=true
            """;


    private final JdbcTemplate jdbc;

    @Autowired
    public EventRepositoryImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Event save(Event e) {
        UUID id = UUID.randomUUID();
        jdbc.update(SAVE_QUERY, id, e.ownerId(), e.title(), e.description(),
                e.durationMinutes(), e.bufferBeforeMinutes(), e.bufferAfterMinutes(),
                EventType.GROUP.equals(e.eventType()), e.maxParticipants(), e.isActive(), UUID.randomUUID(),
                Timestamp.from(e.startDate()), Timestamp.from(e.endDate()), Timestamp.from(e.createdAt()),
                Timestamp.from(e.updatedAt()));

        return getEventById(id).orElseThrow();
    }

    @Override
    public Event regenerateSlug(UUID id) {
        UUID newSlug = UUID.randomUUID();
        jdbc.update(UPDATE_SLUG_QUERY, newSlug, id);

        return getEventById(id).orElseThrow();
    }

    @Override
    public Optional<Event> getEventById(UUID id) {
        try {
            return Optional.of(
                    jdbc.queryForObject(FIND_BY_ID_QUERY,
                            (res, num) -> new Event(
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
                                    res.getTimestamp("endDate").toInstant(),
                                    res.getTimestamp("createdAt").toInstant(),
                                    res.getTimestamp("updatedAt").toInstant()
                            ),
                            id)
            );

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void update(Event e) {
        jdbc.update(UPDATE_QUERY, e.title(), e.description(),
                e.durationMinutes(), e.bufferBeforeMinutes(), e.bufferAfterMinutes(),
                EventType.GROUP.equals(e.eventType()), e.maxParticipants(), e.isActive(),
                Timestamp.from(e.startDate()), Timestamp.from(e.endDate()),
                e.id());

        getEventById(e.id()).orElseThrow();
    }

    @Override
    public List<Event> getAllEvents(UUID ownerId) {
        List<Map<String, Object>> rows = jdbc.queryForList(GET_ALL_ACTIVE_EVENTS, ownerId);
        List<Event> events = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            Event event = new Event(
                    (UUID) row.get("id"),
                    (UUID) row.get("ownerId"),
                    (String) row.get("title"),
                    (String) row.get("description"),
                    (boolean) row.get("isActive"),
                    (int) row.get("maxParticipants"),
                    (int) row.get("durationMinutes"),
                    (int) row.get("bufferBeforeMinutes"),
                    (int) row.get("bufferAfterMinutes"),
                    (EventType) row.get("eventType"),
                    (String) row.get("slug"),
                    ((Timestamp) row.get("startDate")).toInstant(),
                    ((Timestamp) row.get("endDate")).toInstant(),
                    ((Timestamp) row.get("createdAt")).toInstant(),
                    ((Timestamp) row.get("updatedAt")).toInstant());
            events.add(event);
        }
        return events;
    }
}