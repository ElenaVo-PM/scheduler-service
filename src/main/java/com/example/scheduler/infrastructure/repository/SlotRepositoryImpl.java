package com.example.scheduler.infrastructure.repository;

import com.example.scheduler.adapters.dto.BookingRequest;
import com.example.scheduler.adapters.dto.BookingResponse;
import com.example.scheduler.domain.exception.NotFoundException;
import com.example.scheduler.domain.model.BookedSlot;
import com.example.scheduler.domain.model.Slot;
import com.example.scheduler.domain.model.User;
import com.example.scheduler.domain.repository.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SlotRepositoryImpl implements SlotRepository {

    private final JdbcTemplate jdbc;

    @Autowired
    public SlotRepositoryImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Transactional
    @Override
    public BookingResponse bookSlot(User user, BookingRequest request) throws IllegalAccessException {
        return privateBookSlot(user, request);
    }

    @Transactional
    @Override
    public BookingResponse bookSlot(BookingRequest request) throws IllegalAccessException {
        return privateBookSlot(null, request);
    }

    @Override
    public Optional<Slot> getSlotById(UUID slotId) {

        final String GET_SLOT_QUERY = """
                SELECT *
                FROM time_slots
                WHERE time_slots.id = ?
                """;

        try {
            return Optional.ofNullable(jdbc.queryForObject(GET_SLOT_QUERY,
                    (res, _) -> new Slot(res.getObject("id", UUID.class),
                            res.getObject("event_template_id", UUID.class),
                            res.getTimestamp("start_time").toInstant(),
                            res.getTimestamp("end_time").toInstant(),
                            res.getBoolean("is_available")),
                    slotId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public Optional<BookedSlot> getBookedSlotById(UUID id) {
        final String GET_BOOKED_SLOT_QUERY = """
                SELECT *
                FROM bookings
                WHERE booking.id = ?
                """;

        Optional<BookedSlot> bookedSlot = Optional.ofNullable(
                jdbc.queryForObject(GET_BOOKED_SLOT_QUERY,
                        (res, _) -> new BookedSlot(res.getObject("id", UUID.class),
                                res.getObject("event_template_id", UUID.class),
                                res.getObject("slot_id", UUID.class),
                                res.getString("invitee_name"),
                                res.getString("invitee_email"),
                                res.getObject("user_id", UUID.class),
                                res.getTimestamp("created_at").toInstant(),
                                res.getTimestamp("updated_at").toInstant(),
                                res.getBoolean("is_canceled")),
                        id)
        );

        return bookedSlot;
    }

    private void addParticipants(User user,
                                 UUID bookedSlotId,
                                 LocalDateTime now,
                                 String anonymousUsername,
                                 String anonymousEmail) {
        final String ADD_PARTICIPANTS = """
                INSERT INTO booking_participants (id,
                booking_id,
                user_id,
                email,
                name,
                status,
                created_at,
                updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        UUID id = UUID.randomUUID();
        UUID userId = user != null ? user.id() : null;
        String username = user != null ? user.username() : anonymousUsername;
        String email = user != null ? user.email() : anonymousEmail;

        jdbc.update(ADD_PARTICIPANTS,
                id,
                bookedSlotId,
                userId,
                username,
                email,
                "PENDING",
                now,
                now
        );
    }

    private BookingResponse privateBookSlot(User user, BookingRequest request) throws IllegalAccessException {
        final String ADD_NEW_BOOKING_QUERY = """
                INSERT INTO bookings (id,
                event_template_id,
                slot_id,
                invitee_name,
                invitee_email,
                is_canceled,
                created_at,
                updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        String username = user == null ? request.name() : user.username();
        String email = user == null ? request.email() : user.email();

        Slot slot = getSlotById(request.slotId())
                .orElseThrow(() -> new NotFoundException("Slot not found"));

        if (slot.isBooked()) {
            throw new IllegalAccessException("Slot already have booked");
        }

        jdbc.update(ADD_NEW_BOOKING_QUERY,
                id,
                request.eventId(),
                request.slotId(),
                username,
                email,
                false,
                now,
                now);

        BookedSlot bookedSlot = getBookedSlotById(id)
                .orElseThrow(() -> new IllegalArgumentException("An error occurred during booked slot search"));

        try {
            addParticipants(user, bookedSlot.id(), now, username, email);
            return new BookingResponse(bookedSlot.id(),
                    bookedSlot.eventId(),
                    bookedSlot.slotId(),
                    slot.startTime(),
                    slot.endTime(),
                    bookedSlot.isCanceled());
        } catch (DataAccessException e) {
            throw new IllegalStateException("Failed to add participants");
        }

    }
}
