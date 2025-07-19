package com.example.scheduler.domain.repository;

import com.example.scheduler.adapters.dto.BookingRequest;
import com.example.scheduler.adapters.dto.BookingResponse;
import com.example.scheduler.domain.model.*;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

public interface SlotRepository {

    BookingResponse bookSlot(Event event, BookingRequest request) throws IllegalAccessException;

    BookingResponse bookSlot(Event event, User user, BookingRequest request) throws IllegalAccessException;

    Optional<Slot> getSlotById(UUID id);

    Optional<BookedSlot> getBookedSlotById(UUID id);

}
