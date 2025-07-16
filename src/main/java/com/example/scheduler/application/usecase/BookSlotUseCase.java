package com.example.scheduler.application.usecase;

import com.example.scheduler.adapters.dto.BookingResponse;
import com.example.scheduler.domain.repository.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookSlotUseCase {

    private final SlotRepository slotRepository;

    @Autowired
    public BookSlotUseCase(SlotRepository repository) {
        this.slotRepository = repository;
    }

    public BookingResponse execute(BookingResponse request) {
        return slotRepository.bookSlot(request);
    }

}
