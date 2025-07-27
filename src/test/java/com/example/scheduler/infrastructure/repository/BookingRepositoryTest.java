package com.example.scheduler.infrastructure.repository;

import com.example.scheduler.domain.fixture.TestBooking;
import com.example.scheduler.domain.model.Booking;
import com.example.scheduler.domain.repository.BookingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;
import java.util.UUID;

@JdbcTest
@ContextConfiguration(classes = {BookingRepositoryImpl.class})
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void getBookingByIdTest() {
        Booking requiredBooking = bookingRepository.getBookingById(TestBooking.getTestBooking().id()).get();

        Assertions.assertEquals("testName", requiredBooking.inviteeName());
    }

    @Test
    void getUnknownIdBooking() {
        Optional<Booking> requiredBooking = bookingRepository.getBookingById(UUID.randomUUID());

        Assertions.assertTrue(requiredBooking.isEmpty());
    }
}
