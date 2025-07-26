package com.example.scheduler.application.usecase;

import com.example.scheduler.domain.model.*;
import com.example.scheduler.domain.repository.AvailabilityRuleRepository;
import com.example.scheduler.domain.repository.EventRepository;
import com.example.scheduler.domain.repository.ProfileRepository;
import com.example.scheduler.domain.repository.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class GenerateSlotsUseCase {
    private final EventRepository eventRepository;
    private final AvailabilityRuleRepository availabilityRuleRepository;
    private final SlotRepository slotRepository;
    private final ProfileRepository profileRepository;

    @Autowired
    public GenerateSlotsUseCase(EventRepository eventRepository,
                                AvailabilityRuleRepository availabilityRuleRepository,
                                SlotRepository slotRepository,
                                ProfileRepository profileRepository) {
        this.eventRepository = eventRepository;
        this.availabilityRuleRepository = availabilityRuleRepository;
        this.slotRepository = slotRepository;
        this.profileRepository = profileRepository;
    }

    public void execute(UUID eventId) {
        List<Slot> slots = new ArrayList<>();
        Event event = eventRepository.getEventById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event hot found"));
        List<AvailabilityRule> availabilityRules = availabilityRuleRepository.getAllRulesByUser(event.ownerId());
        Profile profile = profileRepository.findByUserId(event.ownerId())
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        ZoneId zone = profile.timezone();

        var currDate = event.startDate().atZone(zone).toLocalDate();
        var endDate = event.endDate() == null ?
                event.startDate().atZone(zone).toLocalDate().plusDays(60L) :
                event.endDate().atZone(zone).toLocalDate();

        while (!currDate.isAfter(endDate)) {
            int dayOfWeek = currDate.getDayOfWeek().getValue();
            for (AvailabilityRule rule : availabilityRules) {
                if (rule.weekday() + 1 == dayOfWeek) {

                    LocalTime intervalStart = rule.startTime();
                    LocalTime intervalEnd = rule.endTime();

                    LocalTime slotStartTime = intervalStart;
                    LocalTime slotEndTime = slotStartTime.plusMinutes(event.durationMinutes());

                    while (!slotEndTime.isAfter(intervalEnd)) {

                        Slot slot = new Slot(UUID.randomUUID(),
                                eventId,
                                LocalDateTime.of(currDate, slotStartTime).atZone(zone).toInstant(),
                                LocalDateTime.of(currDate, slotEndTime).atZone(zone).toInstant(),
                                true);

                        slotStartTime = slotEndTime;
                        slotEndTime = slotStartTime.plusMinutes(event.durationMinutes());
                        slots.add(slot);
                    }
                }
            }
            currDate = currDate.plusDays(1L);
        }

        slotRepository.saveSlots(slots);
    }
}
