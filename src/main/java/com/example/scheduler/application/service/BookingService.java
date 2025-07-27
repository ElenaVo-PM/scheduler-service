package com.example.scheduler.application.service;

import com.example.scheduler.adapters.dto.BookingGeneralInfo;
import com.example.scheduler.domain.exception.NotFoundException;
import com.example.scheduler.domain.model.Booking;
import com.example.scheduler.domain.model.Event;
import com.example.scheduler.domain.model.Slot;
import com.example.scheduler.domain.repository.BookingRepository;
import com.example.scheduler.domain.repository.EventRepository;
import com.example.scheduler.domain.repository.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final SlotRepository slotRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, EventRepository eventRepository,
                          SlotRepository slotRepository) {
        this.bookingRepository = bookingRepository;
        this.eventRepository = eventRepository;
        this.slotRepository = slotRepository;
    }

    @Transactional
    public BookingGeneralInfo getOneBooking(UUID bookingId) {
        Booking requiredBooking = bookingRepository.getBookingById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));
        Event bookingEvent = eventRepository.getEventById(requiredBooking.eventTemplateId()).orElseThrow();
        Slot bookingSlot = slotRepository.getSlotById(requiredBooking.slotId()).orElseThrow();
        return mapToGeneralInfo(requiredBooking, bookingEvent, bookingSlot);
    }

    private BookingGeneralInfo mapToGeneralInfo(Booking booking, Event event, Slot slot) {
        return new BookingGeneralInfo(event.title(), booking.inviteeName(), booking.inviteeEmail(),
                slot.startTime(), slot.endTime(), booking.isCanceled(), booking.createdAt());
    }
}
