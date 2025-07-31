package com.example.scheduler.adapters.web.event;

import com.example.scheduler.adapters.dto.BookingGeneralInfo;
import com.example.scheduler.adapters.dto.BookingRequest;
import com.example.scheduler.adapters.dto.BookingResponse;
import com.example.scheduler.application.service.BookingService;
import com.example.scheduler.application.usecase.BookSlotUseCase;
import com.example.scheduler.application.usecase.CancelBookingUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);
    private final BookSlotUseCase bookSlotUseCase;
    private final CancelBookingUseCase cancelBookingUseCase;
    private final BookingService bookingService;
  
    @Autowired
    public BookingController(BookSlotUseCase bookSlotUseCase, CancelBookingUseCase cancelBookingUseCase, 
                             BookingService bookingService) {
        this.bookSlotUseCase = bookSlotUseCase;
        this.cancelBookingUseCase = cancelBookingUseCase;
        this.bookingService = bookingService;

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
    public ResponseEntity<BookingGeneralInfo> getBooking(@PathVariable UUID bookingId) {
        log.info("Request for booking id {}", bookingId);
        BookingGeneralInfo bookingInfoResponse = bookingService.getOneBooking(bookingId);
        log.debug("Get booking {}", bookingInfoResponse.toString());
        return ResponseEntity.ok(bookingInfoResponse);
    }

    /**
     * DELETE /booking/{bookingId} - Отмена бронирования
     */
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> cancelBooking(@PathVariable UUID bookingId) {
        cancelBookingUseCase.execute(bookingId);
        return ResponseEntity.noContent().build();
    }
}
