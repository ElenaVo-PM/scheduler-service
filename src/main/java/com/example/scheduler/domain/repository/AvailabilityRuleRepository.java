package com.example.scheduler.domain.repository;

import com.example.scheduler.domain.model.AvailabilityRule;

public interface AvailabilityRuleRepository {

    AvailabilityRule save(AvailabilityRule rule);

    boolean intersects(AvailabilityRule rule);
}