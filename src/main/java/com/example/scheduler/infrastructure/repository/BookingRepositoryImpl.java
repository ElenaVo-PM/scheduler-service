package com.example.scheduler.infrastructure.repository;

import com.example.scheduler.domain.model.Booking;
import com.example.scheduler.domain.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class BookingRepositoryImpl implements BookingRepository {
    private final JdbcTemplate jdbc;
    private final RowMapper<Booking> rowMapper;

    @Autowired
    public BookingRepositoryImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.rowMapper = new DataClassRowMapper<>(Booking.class);
    }


    public Optional<Booking> getBookingById(UUID bookingId) {
        final String FIND_BOOKING_BY_ID = "SELECT * FROM bookings WHERE id = ?";
        try {
            return Optional.ofNullable(jdbc.queryForObject(FIND_BOOKING_BY_ID, rowMapper, bookingId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }
}
