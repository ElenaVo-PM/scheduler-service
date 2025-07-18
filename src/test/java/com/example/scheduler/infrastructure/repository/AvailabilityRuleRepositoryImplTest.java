package com.example.scheduler.infrastructure.repository;

import com.example.scheduler.domain.model.AvailabilityRule;
import com.example.scheduler.domain.model.User;
import com.example.scheduler.domain.repository.AvailabilityRuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@ContextConfiguration(classes = {AvailabilityRuleRepositoryImpl.class, UserRepositoryImpl.class})
class AvailabilityRuleRepositoryImplTest {

    @Autowired
    private AvailabilityRuleRepository ruleRepository;
    @Autowired
    private UserRepositoryImpl userRepository;
    private User user;
    AvailabilityRule rule;

    @BeforeEach
    void setUp() {
        user = userRepository.save("username", "password", "email@ex.com");

        rule = new AvailabilityRule(UUID.randomUUID(), user.id(), 1,
                LocalTime.of(12, 0), LocalTime.of(13, 0),
                Instant.now(), Instant.now());
        ruleRepository.save(rule);
    }

    @Test
    void whenTimeEqualsSavedTime_thenIntersected() {
        assertTrue(ruleRepository.intersects(rule));
    }

    @Test
    void whenSavedTimeIsInside_thenIntersected() {
        AvailabilityRule intersectedRule = new AvailabilityRule(UUID.randomUUID(), user.id(), 1,
                LocalTime.of(0,0), LocalTime.of(23,59),
                Instant.now(), Instant.now());
        assertTrue(ruleRepository.intersects(intersectedRule));
    }

    @Test
    void whenStartTimeIsBeforeSavedEndTime_thenIntersected() {
        AvailabilityRule intersectedRule = new AvailabilityRule(UUID.randomUUID(), user.id(), 1,
                LocalTime.of(12,30), LocalTime.of(13,0),
                Instant.now(), Instant.now());
        assertTrue(ruleRepository.intersects(intersectedRule));
    }

    @Test
    void whenEndTimeIsAfterSavedStartTime_thenIntersected() {
        AvailabilityRule intersectedRule = new AvailabilityRule(UUID.randomUUID(), user.id(), 1,
                LocalTime.of(12,0), LocalTime.of(12,1),
                Instant.now(), Instant.now());
        assertTrue(ruleRepository.intersects(intersectedRule));
    }

    @Test
    void whenEndTimeIsBeforeSavedStartTime_thenNonIntersected() {
        AvailabilityRule nonIntersectedRule = new AvailabilityRule(UUID.randomUUID(), user.id(), 1,
                LocalTime.of(11,0), LocalTime.of(11,59),
                Instant.now(), Instant.now());
        assertFalse(ruleRepository.intersects(nonIntersectedRule));
    }

    @Test
    void whenStartTimeIsAfterSavedEndTime_thenNonIntersected() {
        AvailabilityRule intersectedRule = new AvailabilityRule(UUID.randomUUID(), user.id(), 1,
                LocalTime.of(13,1), LocalTime.of(14,0),
                Instant.now(), Instant.now());
        assertFalse(ruleRepository.intersects(intersectedRule));
    }

    @Test
    void whenEndTimeEqualsSavedStartTime_thenNonIntersected() {
        AvailabilityRule nonIntersectedRule = new AvailabilityRule(UUID.randomUUID(), user.id(), 1,
                LocalTime.of(11,0), LocalTime.of(12,0),
                Instant.now(), Instant.now());
        assertFalse(ruleRepository.intersects(nonIntersectedRule));
    }

    @Test
    void whenStartTimeEqualsSavedEndTime_thenNonIntersected() {
        AvailabilityRule nonIntersectedRule = new AvailabilityRule(UUID.randomUUID(), user.id(), 1,
                LocalTime.of(13,0), LocalTime.of(14,0),
                Instant.now(), Instant.now());
        assertFalse(ruleRepository.intersects(nonIntersectedRule));
    }
}