package com.example.scheduler.infrastructure.repository;

import com.example.scheduler.domain.model.Booking;
import com.example.scheduler.domain.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.Optional;
import java.util.UUID;

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
        SqlParameterSource params = new ExtendedBeanPropertySqlParameterSource(bookingId);
        return Optional.ofNullable(jdbc.queryForObject(FIND_BOOKING_BY_ID, rowMapper, params));
    }
}
