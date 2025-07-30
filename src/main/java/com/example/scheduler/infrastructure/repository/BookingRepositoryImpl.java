package com.example.scheduler.infrastructure.repository;

import com.example.scheduler.domain.model.Booking;
import com.example.scheduler.domain.model.TimeInterval;
import com.example.scheduler.domain.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BookingRepositoryImpl implements BookingRepository {
    private final JdbcTemplate jdbc;
    private final RowMapper<Booking> bookingRowMapper;
    private final RowMapper<TimeInterval> timeIntervalRowMapper;

    @Autowired
    public BookingRepositoryImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.bookingRowMapper = new DataClassRowMapper<>(Booking.class);
        this.timeIntervalRowMapper = new DataClassRowMapper<>(TimeInterval.class);

    }

    private final String FIND_BOOKING_BY_ID = """
            SELECT id, event_template_id, slot_id, invitee_name,
             invitee_email, is_canceled, created_at, updated_at FROM bookings WHERE id = ?
            """;

    private final String GET_TIME_OF_BOOKINGS_FOR_USER_BY_ID = """
            SELECT b.start_time, b.end_time FROM booking_participants AS bp
            LEFT JOIN bookings AS b ON b.id = bp.booking_id
            WHERE bp.user_id = ?
            """;

    @Override
    public Optional<Booking> getBookingById(UUID bookingId) {
        try {
            return Optional.ofNullable(jdbc.queryForObject(FIND_BOOKING_BY_ID, bookingRowMapper, bookingId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<TimeInterval> getTimeOfBookingsForUser(UUID userId) {
        return jdbc.query(GET_TIME_OF_BOOKINGS_FOR_USER_BY_ID, timeIntervalRowMapper, userId);
    }
}
