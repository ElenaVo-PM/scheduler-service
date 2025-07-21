package com.example.scheduler.adapters.web.event;

import com.example.scheduler.adapters.dto.BookingRequest;
import com.example.scheduler.adapters.dto.BookingResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {

    /**
     * POST /booking - Создание брони
     */
    @PostMapping
    public ResponseEntity<BookingResponse> bookSlot(@RequestBody BookingRequest request) {
        return ResponseEntity.ok(null);
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
