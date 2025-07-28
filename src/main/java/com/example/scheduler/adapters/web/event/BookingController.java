package com.example.scheduler.adapters.web.event;

import com.example.scheduler.adapters.dto.BookingRequest;
import com.example.scheduler.adapters.dto.BookingResponse;
import com.example.scheduler.application.usecase.BookSlotUseCase;
import com.example.scheduler.application.usecase.CancelBookingUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final BookSlotUseCase bookSlotUseCase;
    private final CancelBookingUseCase cancelBookingUseCase;

    @Autowired
    public BookingController(BookSlotUseCase bookSlotUseCase, CancelBookingUseCase cancelBookingUseCase) {
        this.bookSlotUseCase = bookSlotUseCase;
        this.cancelBookingUseCase = cancelBookingUseCase;
    }

    /**
     * POST /booking - Создание брони
     */
    @PostMapping
    public ResponseEntity<BookingResponse> bookSlot(@RequestBody BookingRequest request) throws IllegalAccessException {

        return ResponseEntity.ok(bookSlotUseCase.execute(request));
    }

    /**
     * GET /booking - Список всех бронирований пользователя
     */
    @GetMapping
    public ResponseEntity<List<BookingResponse>> getBookings() {
        return ResponseEntity.ok(null);
    }

    /**
     * GET /booking/{bookingId} - Получение конкретного бронирования
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(null);
    }

    /**
     * DELETE /booking/{bookingId} - Отмена бронирования
     */
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> cancelBooking(@PathVariable UUID bookingId) throws IllegalAccessException {
        cancelBookingUseCase.execute(bookingId);
        return ResponseEntity.noContent().build();
    }
}
