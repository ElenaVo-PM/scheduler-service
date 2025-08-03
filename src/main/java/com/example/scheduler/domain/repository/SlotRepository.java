package com.example.scheduler.domain.repository;

import com.example.scheduler.adapters.dto.BookingRequest;
import com.example.scheduler.adapters.dto.BookingResponse;
import com.example.scheduler.domain.model.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SlotRepository {

    BookingResponse bookSlot(Event event, BookingRequest request) throws IllegalAccessException;

    BookingResponse bookSlot(Event event, User user, BookingRequest request) throws IllegalAccessException;

    Optional<Slot> getSlotById(UUID id);

    List<Slot> getAllSlotsForEvent(UUID eventId);

    void saveSlots(List<Slot> slots);
}
