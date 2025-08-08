package com.example.scheduler.infrastructure.repository;

import com.example.scheduler.domain.model.Event;
import com.example.scheduler.domain.model.EventType;
import com.example.scheduler.domain.repository.EventRepository;
import com.example.scheduler.infrastructure.mapper.EventRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
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

    private static final String GET_EVENT_BY_SLUG = """
            SELECT id,
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
            WHERE slug = CAST(? AS TEXT)
            """;
    private static final String TOGGLE_EVENT_QUERY = """
            UPDATE event_templates
            SET is_active = ?
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

    private static final String DELETE_QUERY = """
            DELETE FROM event_templates
            WHERE id = ?
            """;

    private final JdbcTemplate jdbc;
    private final EventRowMapper mapper;

    @Autowired
    public EventRepositoryImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.mapper = new EventRowMapper();
    }

    @Override
    public Event save(Event e) {
        UUID id = UUID.randomUUID();
        jdbc.update(SAVE_QUERY, id, e.ownerId(), e.title(), e.description(),
                e.durationMinutes(), e.bufferBeforeMinutes(), e.bufferAfterMinutes(),
                EventType.GROUP.equals(e.eventType()), e.maxParticipants(), e.isActive(), UUID.randomUUID(),
                Timestamp.from(e.startDate()), e.endDate() == null ? null : Timestamp.from(e.endDate()),
                Timestamp.from(e.createdAt()), Timestamp.from(e.updatedAt()));

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
            return Optional.ofNullable(
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
                                    res.getTimestamp("endDate") == null
                                            ? null
                                            : res.getTimestamp("endDate").toInstant(),
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
    public Event toggleActiveEvent(UUID id) {
        Event event = getEventById(id).orElseThrow();
        boolean toggle = true;

        if (event.isActive()) {
            jdbc.update(TOGGLE_EVENT_QUERY, !toggle, id);
        } else {
            jdbc.update(TOGGLE_EVENT_QUERY, toggle, id);
        }

        return getEventById(id).orElseThrow();
    }

    @Override
    public void update(Event e) {
        jdbc.update(UPDATE_QUERY, e.title(), e.description(),
                e.durationMinutes(), e.bufferBeforeMinutes(), e.bufferAfterMinutes(),
                EventType.GROUP.equals(e.eventType()), e.maxParticipants(), e.isActive(),
                Timestamp.from(e.startDate()), e.endDate() == null ? null : Timestamp.from(e.endDate()),
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
                    (boolean) row.get("is_group_event") ? EventType.GROUP : EventType.ONE2ONE,
                    (String) row.get("slug"),
                    ((Timestamp) row.get("startDate")).toInstant(),
                    row.get("endDate") == null ? null : ((Timestamp) row.get("endDate")).toInstant(),
                    ((Timestamp) row.get("createdAt")).toInstant(),
                    ((Timestamp) row.get("updatedAt")).toInstant());
            events.add(event);
        }
        return events;
    }

    @Override
    public void delete(UUID id) {
        jdbc.update(DELETE_QUERY, id);
    }

    @Override
    public Optional<Event> getEventBySlug(UUID slug) {
        return Optional.ofNullable(jdbc.queryForObject(GET_EVENT_BY_SLUG, mapper, slug));
    }
}
