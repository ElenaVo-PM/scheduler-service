package com.example.scheduler.application.service;

import com.example.scheduler.adapters.dto.BookingGeneralInfo;
import com.example.scheduler.domain.exception.NotFoundException;
import com.example.scheduler.domain.fixture.TestBooking;
import com.example.scheduler.domain.fixture.TestEvents;
import com.example.scheduler.domain.fixture.TestSlot;
import com.example.scheduler.domain.model.Booking;
import com.example.scheduler.domain.model.Event;
import com.example.scheduler.domain.model.Slot;
import com.example.scheduler.domain.repository.BookingRepository;
import com.example.scheduler.domain.repository.EventRepository;
import com.example.scheduler.domain.repository.SlotRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

public class BookingServiceTest {
    private BookingRepository mockBookingRepository;
    private EventRepository mockEventRepository;
    private SlotRepository mockSlotRepository;
    private BookingService bookingService;
    Booking testBooking;
    Event testEvent;
    Slot testSlot;

    @BeforeEach
    void setUp() {
        mockBookingRepository = Mockito.mock(BookingRepository.class);
        mockEventRepository = Mockito.mock(EventRepository.class);
        mockSlotRepository = Mockito.mock(SlotRepository.class);
        bookingService = new BookingService(mockBookingRepository, mockEventRepository, mockSlotRepository);
        testBooking = TestBooking.getTestBooking();
        testEvent = TestEvents.demo();
        testSlot = TestSlot.getTestSlot();
    }

    @Test
    void getBookingByIdTest() {
        Mockito.when(mockBookingRepository.getBookingById(Mockito.any(UUID.class))).thenReturn(Optional.of(testBooking));
        Mockito.when(mockEventRepository.getEventById(Mockito.any(UUID.class))).thenReturn(Optional.of(testEvent));
        Mockito.when(mockSlotRepository.getSlotById(Mockito.any(UUID.class))).thenReturn(Optional.of(testSlot));

        BookingGeneralInfo testBookingInfo = bookingService.getOneBooking(UUID.randomUUID());

        Assertions.assertEquals(testEvent.title(), testBookingInfo.eventName());
        Assertions.assertEquals(testSlot.startTime(), testBookingInfo.startTime());
    }

    @Test
    void getUnknownIdBookingTest() {
        Mockito.when(mockBookingRepository.getBookingById(UUID.randomUUID())).thenReturn(Optional.of(testBooking));
        Mockito.when(mockEventRepository.getEventById(Mockito.any(UUID.class))).thenReturn(Optional.of(testEvent));

        Assertions.assertThrows(NotFoundException.class, () -> bookingService.getOneBooking(UUID.randomUUID()));
    }
}
