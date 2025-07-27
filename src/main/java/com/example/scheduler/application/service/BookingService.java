package com.example.scheduler.application.service;

import com.example.scheduler.adapters.dto.BookingGeneralInfo;
import com.example.scheduler.domain.exception.NotFoundException;
import com.example.scheduler.domain.model.Booking;
import com.example.scheduler.domain.model.Event;
import com.example.scheduler.domain.repository.BookingRepository;
import com.example.scheduler.domain.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, EventRepository eventRepository) {
        this.bookingRepository = bookingRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional
    public BookingGeneralInfo getOneBooking(UUID bookingId) {
        Booking requiredBooking = bookingRepository.getBookingById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));
        Event bookingEvent = eventRepository.getEventById(requiredBooking.getEventTemplateId()).orElseThrow();
        return mapToGeneralInfo(requiredBooking, bookingEvent);
    }

    private BookingGeneralInfo mapToGeneralInfo(Booking booking, Event event) {
        return new BookingGeneralInfo(event.title(), booking.getInviteeName(), booking.getInviteeEmail(),
                booking.getStartTime(), booking.getEndTime(), booking.isCanceled(), booking.getCreatedAt());
    }
}
