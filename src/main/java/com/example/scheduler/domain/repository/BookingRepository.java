package com.example.scheduler.domain.repository;


import com.example.scheduler.domain.model.Booking;
import com.example.scheduler.domain.model.TimeInterval;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository {
    Optional<Booking> getBookingById(UUID bookingId);

    List<TimeInterval> getTimeOfBookingsForUser(UUID userId);
}
