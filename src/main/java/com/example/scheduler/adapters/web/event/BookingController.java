package com.example.scheduler.adapters.web.event;

import com.example.scheduler.adapters.dto.BookingResponse;
import com.example.scheduler.application.usecase.BookSlotUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final BookSlotUseCase bookSlotUseCase;

    @Autowired
    public BookingController(BookSlotUseCase bookSlotUseCase) {
        this.bookSlotUseCase = bookSlotUseCase;
    }

    /**
     * POST /booking - Создание брони
     */
    @PostMapping
    public ResponseEntity<BookingResponse> bookSlot(@RequestBody BookingResponse request) {
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
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId) {
        return ResponseEntity.noContent().build();
    }

}
