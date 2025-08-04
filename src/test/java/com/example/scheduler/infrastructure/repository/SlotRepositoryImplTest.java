package com.example.scheduler.infrastructure.repository;


import com.example.scheduler.AbstractTestContainerTest;
import com.example.scheduler.adapters.dto.BookingRequest;
import com.example.scheduler.adapters.dto.BookingResponse;
import com.example.scheduler.domain.model.Booking;
import com.example.scheduler.domain.model.Event;
import com.example.scheduler.domain.model.Slot;
import com.example.scheduler.domain.model.User;
import com.example.scheduler.domain.repository.EventRepository;
import com.example.scheduler.domain.repository.SlotRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class SlotRepositoryImplTest extends AbstractTestContainerTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private SlotRepository repository;
    @Autowired
    private EventRepository eventRepository;

    private UUID eventId;
    private UUID slotId;
    private UUID userId;

    @BeforeAll
    void setUp() {

        eventId = UUID.randomUUID();
        slotId = UUID.randomUUID();
        userId = UUID.randomUUID();

        jdbc.update("""
                    INSERT INTO users (id, email, password_hash, username, role, created_at, updated_at)
                    VALUES (?, ?, ?, ?, ?, now(), now())
                """, userId, "test@example.com", "hashed-password", "testuser", "USER");

        jdbc.update("""
                            INSERT INTO event_templates (
                                id, user_id, title, description,
                                duration_minutes, buffer_before_minutes, buffer_after_minutes,
                                is_group_event, max_participants, is_active, slug,
                                start_date, end_date, created_at, updated_at
                            )
                            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), now())
                        """, eventId, userId, "Title", "Description",
                30, 0, 0, true, 4, true, UUID.randomUUID(),
                Timestamp.from(Instant.now()),
                Timestamp.from(Instant.now().plus(Duration.ofDays(1)))
        );
        jdbc.update("""
                            INSERT INTO time_slots (
                                id, event_template_id, start_time, end_time, is_available, created_at, updated_at
                            )
                            VALUES (?, ?, ?, ?, ?, now(), now())
                        """, slotId, eventId,
                Timestamp.from(Instant.now().plus(Duration.ofHours(1))),
                Timestamp.from(Instant.now().plus(Duration.ofHours(2))),
                true
        );
    }

    @Test
    @DisplayName("Authorized user successfully booked a slot")
    void bookSlotWithUserSuccess() throws Exception {
        User user = new User(
                userId,
                "TestUser",
                "user@example.com",
                "password",
                "USER",
                Instant.now(),
                Instant.now()
        );

        BookingRequest request = new BookingRequest(
                eventId, slotId, user.email(), user.username()
        );

        Event event = eventRepository.getEventById(eventId).get();
        BookingResponse response = repository.bookSlot(event, user, request);

        assertNotNull(response);
        assertEquals(slotId, response.slotId());
        assertFalse(response.isCanceled());
    }

    @Test
    @DisplayName("Unauthorized user successfully booked a slot")
    void bookSlotAnonymousSuccess() throws Exception {
        BookingRequest request = new BookingRequest(
                eventId, slotId, "email@email.con", "anonymous"
        );

        Event event = eventRepository.getEventById(eventId).get();

        BookingResponse response = repository.bookSlot(event, request);

        assertNotNull(response);
        assertEquals(slotId, response.slotId());
        assertFalse(response.isCanceled());
    }

    @Test
    @DisplayName("Get slot by ID")
    void getSlotByIdExistingId() {
        Optional<Slot> result = repository.getSlotById(slotId);

        assertTrue(result.isPresent());
        assertEquals(slotId, result.get().id());
        assertTrue(result.get().isAvailable());
    }

    @Test
    @DisplayName("Get empty result on not-existing slot")
    void getSlotByIdNonExistingId() {
        Optional<Slot> result = repository.getSlotById(UUID.randomUUID());
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Cancel existing booking successfully")
    void cancelBookingSuccess() throws Exception {
        User user = new User(
                userId,
                "TestUser",
                "user@example.com",
                "password",
                "USER",
                Instant.now(),
                Instant.now()
        );

        BookingRequest request = new BookingRequest(
                eventId, slotId, user.email(), user.username()
        );

        Event event = eventRepository.getEventById(eventId).get();

        BookingResponse bookingResponse = repository.bookSlot(event, user, request);

        UUID bookingId = bookingResponse.id();
        Optional<Booking> bookingOpt = repository.findBookingById(bookingId);
        assertTrue(bookingOpt.isPresent());
        assertFalse(bookingOpt.get().isCanceled());

        Instant cancelTime = Instant.now();
        repository.cancelBooking(bookingId, cancelTime);

        Optional<Booking> canceledBookingOpt = repository.findBookingById(bookingId);
        assertTrue(canceledBookingOpt.isPresent());
        assertTrue(canceledBookingOpt.get().isCanceled());
    }

    @Test
    @DisplayName("Cancel non-existing booking does nothing")
    void cancelBookingNonExisting() {
        UUID fakeBookingId = UUID.randomUUID();

        assertDoesNotThrow(() -> repository.cancelBooking(fakeBookingId, Instant.now()));

        Optional<Booking> bookingOpt = repository.findBookingById(fakeBookingId);
        assertTrue(bookingOpt.isEmpty());
    }

    @Test
    @DisplayName("Cancel already canceled booking does nothing")
    void cancelBookingAlreadyCanceled() throws Exception {
        User user = new User(
                userId,
                "TestUser",
                "user@example.com",
                "password",
                "USER",
                Instant.now(),
                Instant.now()
        );

        BookingRequest request = new BookingRequest(
                eventId, slotId, user.email(), user.username()
        );

        Event event = eventRepository.getEventById(eventId).get();

        BookingResponse bookingResponse = repository.bookSlot(event, user, request);

        UUID bookingId = bookingResponse.id();

        repository.cancelBooking(bookingId, Instant.now());

        Optional<Booking> canceledBookingOpt = repository.findBookingById(bookingId);
        assertTrue(canceledBookingOpt.isPresent());
        assertTrue(canceledBookingOpt.get().isCanceled());

        assertDoesNotThrow(() -> repository.cancelBooking(bookingId, Instant.now()));

        Optional<Booking> canceledBookingOpt2 = repository.findBookingById(bookingId);
        assertTrue(canceledBookingOpt2.isPresent());
        assertTrue(canceledBookingOpt2.get().isCanceled());
    }

    @Test
    void getSlotsForEvent() {
        List<Slot> eventSlots = repository.getAllSlotsForEvent(eventId);
        assertEquals(1, eventSlots.size());
        assertEquals(slotId, eventSlots.getFirst().id());
    }
}