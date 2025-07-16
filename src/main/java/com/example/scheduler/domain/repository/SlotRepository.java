package com.example.scheduler.domain.repository;

import com.example.scheduler.adapters.dto.BookingResponse;

public interface SlotRepository {

    BookingResponse bookSlot(BookingResponse request);
}
