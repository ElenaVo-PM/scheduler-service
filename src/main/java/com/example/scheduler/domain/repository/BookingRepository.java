package com.example.scheduler.domain.repository;


import com.example.scheduler.domain.model.Booking;

import java.util.Optional;
import java.util.UUID;

public interface BookingRepository {
        Optional<Booking> getBookingById (UUID bookingId);
}
